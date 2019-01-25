/**
 * 
 */
package com.crm.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.crm.dao.MySQLConnect;
import com.crm.models.strategy.DeleteNothing;
import com.crm.models.strategy.DeleteStrategyInterface;

/**
 * @author A�lion
 *
 */
public abstract class Model implements ModelInterface<Model> {

	/**
	 * @var String Nom de l'entit�
	 */
	protected String entityName;
	
	/**
	 * @var int Identifiant d'une ligne du mod�le
	 */
	protected int id;
	
	/**
	 * @var Repository Liste des objets de mod�le
	 * Couplage faible (le repository continue � exister m�me si je d�truis Model)
	 */
	protected Repository repository;
	
	/**
	 * Instance de connexion � la base de donn�es
	 */
	protected Connection connexion;

	private Field field;
	
	protected PreparedStatement insert;
	
	/**
	 * Collectionner tous les attributs des classes de type ArrayList<Model>
	 */
	private ArrayList<Field> collections = new ArrayList<Field>();
	
	/**
	 * Instance de requ�te pr�par�e :
	 * SELECT id FROM [entityName] ORDER BY id DESC LIMIT 0,1
	 */
	protected PreparedStatement lastId;
	
	protected PreparedStatement deleteStatement;
	
	/**
	 * Strat�gie de suppression � adopter
	 */
	private DeleteStrategyInterface deleteStrategy;
	
	/**
	 * Constructeur avec DI (Dependency Injection / Injection de d�pendance)
	 * @param repository
	 * @throws SQLException 
	 */
	public Model(Repository repository) throws SQLException {
		
		this.entityName = this._getEntityName();
		
		this.repository = repository;
		
		this.connexion = MySQLConnect.getConnexion();
		
		// Pr�parer toutes les requ�tes...
		this.insert = this.preparedInsert();
		this.lastId = this.lastId();
		this.deleteStatement = this.preparedDelete();
		
		// Strat�gie par d�faut pour les suppressions : DeleteNothing
		this.deleteStrategy = new DeleteNothing();
		
		this.hasModelCollection();
	}

	public Model() {}
	public Repository getRepository() {
		return this.repository;
	}
	
	public void setDeleteStrategy(DeleteStrategyInterface deleteStrategy) {
		this.deleteStrategy = deleteStrategy;
	}
	
	public String toString() {
		String reflexion = "Nom de l'entit� : " + this.entityName + "\n";
		
		return reflexion;
	}
	
	/* (non-Javadoc)
	 * @see com.crm.models.ModelInterface#find(int)
	 */
	@Override
	public Model find(int id) {
		return this.repository.find(id);
	}

	abstract public String name();
	
	abstract public int id();
	
	/* (non-Javadoc)
	 * @see com.crm.models.ModelInterface#find()
	 */
	@Override
	public HashMap<Integer, Model> find() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crm.models.ModelInterface#persist()
	 */
	@Override
	public Model persist() throws IllegalArgumentException, IllegalAccessException, SQLException {
		PreparedStatement query = this.insert;
		
		// Utilisation des requ�tes pr�par�es
		// 1. Remplacer les placeholders par leur valeur
		int indice = 1;
		
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.getGenericType().getTypeName().contains("String")) {
				this.insert.setString(indice, field.get(this).toString());
			} else if (field.getGenericType().getTypeName().contains("int")) {
				this.insert.setInt(indice, (int) field.get(this));
			} else if (field.getGenericType().getTypeName().contains("Model") &&
					!field.getGenericType().getTypeName().contains("ArrayList")
					) {
				this.insert.setInt(indice, ((Model) field.get(this)).id);
			}
			indice++; // Ne pas oublier d'incr�menter le compteur...
		}
		// 2. Enfin... ex�cuter la requ�te finalis�e
		//System.out.println("Requ�te : " + query.toString());
		query.executeUpdate();
		
		// 3. Dans tous les cas, on ne sait jamais, r�cup�re le dernier id
		ResultSet res = this.lastId.executeQuery();
		
		if (res.first()) {
			//System.out.println("R�cup�re le dernier id");
			int lastId = res.getInt("id");

			// Ne pas oublier de d�finir l'id du mod�le
			this.id = lastId;
			// Au passage, on ajoute au Repository
			this.repository.put(this);
			
			if (this.collections.size() > 0) {
				// So what ?
				for (Field field : this.collections) {
					//System.out.println("Boucle sur les collections");
					// R�cup�rer la valeur de ce field
					ArrayList<Model> values = (ArrayList<Model>) field.get(this);
					// Boucler sur les valeurs... et faire le job
					for (Model value : values) {
						// R�cup�rer la m�thode concern�e (celle qui va injecter CompanyModel dans ContactModel)
						ManyToOne method = value.getClass().getAnnotation(ManyToOne.class);
						
						//System.out.println("La m�thode : "  + method.name());
						
						// R�cup�rer la m�thode � partir de la classe
						try {
							Method manyToOne = value.getClass().getDeclaredMethod(method.name(), Model.class);
							// Invoquer la m�thode en lui passant les param�tres �ventuels
							try {
								manyToOne.invoke(value, this);
								value.persist();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.crm.models.ModelInterface#remove()
	 */
	@Override
	public boolean remove() {
		try {
			this.deleteStatement.setInt(1, this.id); // Affecte la valeur de l'identifiant
			
			// Hola, on v�rifie avant qu'il n'y ait pas des enfants
			if (this.collections.size() == 0) {
				this.deleteStatement.executeUpdate();
				return true;				
			} else {
				// En fonction de la strat�gie d�finie
				if (this.deleteStrategy instanceof DeleteNothing) {
					// Okay, mais si jamais je n'ai aucun enfant
					// Pourquoi je ne pourrais pas supprimer ? hein ?
					if (!this._hasChildren()) {
						this.deleteStatement.executeUpdate();
						return true;
					}
					return false;
				} else {
					for (Field field : this.collections) {
						try {
							ArrayList<Model> values = (ArrayList<Model>) field.get(this);
							for (Model model : values) {
								model.remove();
							}
							this.deleteStatement.executeUpdate();
							return true;
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * G�n�re une requ�te de type SELECT sur un mod�le
	 * @return String
	 * @throws SQLException 
	 */
	public ResultSet select() throws SQLException {
		Statement statement =  this.connexion.createStatement();
		return statement.executeQuery("SELECT id," + this._getAttributes() + " FROM " + this.entityName + ";");
	}
	
	public abstract Model hydrate(ResultSet row) throws NoSuchMethodException, SecurityException, SQLException;
	
	private PreparedStatement preparedDelete() throws SQLException {
		String query = "DELETE FROM " + this.entityName + " WHERE id=?;";
		return this.connexion.prepareStatement(query);
	}
	
	private PreparedStatement preparedInsert() throws SQLException {
		String query = "INSERT INTO " + this.entityName + "(" +
				this._getAttributes() + ") VALUES (";
		// R�cup�rer le nombre d'attributs et g�rer les "placeholders"
		for (int i=0; i < this._getAttributesNumber(); i++) {
			query += "?,";
		}
		// Ne pas oublier de supprimer la derni�re virgule en trop
		query = query.substring(0, query.length()-1);
		
		// A la fin, on termine la requ�te
		query += ");";
		
		PreparedStatement statement = this.connexion.prepareStatement(query);
		
		//System.out.println("Requ�te pr�par�e : " + query);
		
		return statement;
	}
	
	private PreparedStatement lastId() {
		String query = "SELECT id FROM " + this.entityName + " ORDER BY id DESC LIMIT 0,1";
		try {
			return this.connexion.prepareStatement(query);
		} catch(SQLException e) {
			System.out.println("Erreur de pr�paration de requ�te : " + query + "[" + e.getMessage() + "]");
			return null;
		}
	}
	
	/**
	 * @obsolete
	 * @return String
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String insert() throws IllegalArgumentException, IllegalAccessException {
		String query = "INSERT INTO " + this.entityName + "(" + this._getAttributes() + ") VALUES (" +
				this._getValues() + ");";
		
		
		try {
			Statement insertStatement;
			insertStatement = this.connexion.createStatement();
			insertStatement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return query;
	}
	
	/**
	 * Retourne le nom de la table � partir de l'annotation
	 * @return String
	 */
	private String _getEntityName() {
		Class<? extends Model> myself = this.getClass();
		
		Table annotation = myself.getAnnotation(Table.class);
		
		if (annotation != null) {
			return annotation.name();
		} else {
			// Let's go to the fuck way to do this
			String shortClassName = myself.getSimpleName().substring(0,  myself.getSimpleName().indexOf("Model")).toLowerCase();
			String className = shortClassName;
			
			if (shortClassName.substring(className.length()-1).equals("y")) {
				shortClassName = shortClassName.substring(0, shortClassName.length() - 1) + "ies";
			} else {
				shortClassName += "s";
			}
			return shortClassName;
		}
	}
	
	private int _getAttributesNumber() {
		Field[] allProperties = this.getClass().getDeclaredFields();
		
		int attributesCounter = 0;
		
		for (Field field : allProperties) {
			if (
					!field.getGenericType().getTypeName().contains("ArrayList")
				) {
				// On ajoute 1 au compteur d'attributs
				attributesCounter++;
			}
		}
		return attributesCounter;
	}
	
	private String _getAttributes()  {
		Field[] allProperties = this.getClass().getDeclaredFields();
		
		String allColumns = "";
		
		// Pour test, on regarde ce qu'il y a dans le tableau
		for (int i=0; i < allProperties.length; i++) {
			Field field = allProperties[i];
			
			//System.out.println(field.getGenericType().getTypeName());
			
			if (
				!field.getGenericType().getTypeName().contains("ArrayList") &&
				!field.getGenericType().getTypeName().contains("Model")
			) {
				allColumns += field.getName() + ",";
			} else {
				if (
						field.getGenericType().getTypeName().contains("Model") &&
						!field.getGenericType().getTypeName().contains("ArrayList")
					) {
					// Traiter sp�cifiquement un champ de type Model
					try {
						/**
						 * Way 1 :  passer par une annotation
						*/
						OneToMany annotation = field.getAnnotation(OneToMany.class);
						
						allColumns += annotation.name() + "_id,";
						
						/**
						// Way 2 : reflexion sur l'objet
						Class<? extends Model> parentClass = (Class<? extends Model>) field.getType();
						// On peut acc�der aux annotations ?
						Table parentAnnotation = parentClass.getAnnotation(Table.class);
						allColumns += parentAnnotation.name() + "_id,";
						
						/* Way 3 : on se d�brouille... comme on peut
						try {
							System.out.println("Entit� de la classe parente : " + ((Model) field.get(this)).entityName);
							allColumns += ((Model) field.get(this)).entityName + "_id";
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						// Way 4 : truc alambiqu�
						
					} catch(IllegalArgumentException e) {
						System.out.println("Argument ill�gal : " + e.getMessage());
					} catch(NullPointerException e) {
						System.out.println("So bad...");
					}
				}
			}
		}
		//System.out.println("A la fin ? " + allColumns);
		return allColumns.substring(0, allColumns.length() - 1);
	}
	
	private String _getValues() throws IllegalArgumentException, IllegalAccessException {
		String allValues = "";
		
		// Pour test, on regarde ce qu'il y a dans le tableau
		for (Field field : this.getClass().getDeclaredFields()) {
			if (
				!field.getGenericType().getTypeName().contains("ArrayList") &&
				!field.getGenericType().getTypeName().contains("Model")
			) {
				allValues += "'" + field.get(this).toString().replaceAll("\\'", "''") + "',";
			}
		}
		return allValues.substring(0, allValues.length() - 1);		
	}
	
	private void hasModelCollection() {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (
					field.getGenericType().getTypeName().contains("ArrayList") &&
					field.getGenericType().getTypeName().contains("Model")
				) {
				// YES... y en a bien un
				// L'ajouter � la collection
				this.collections.add(field);
			}
		}
	}
	
	private boolean _hasChildren() {
		boolean hasChildren = false;
		
		for (Field field : this.collections) {
			try {
				ArrayList<Model> values = (ArrayList<Model>) field.get(this);
				if (values.size() > 0) {
					hasChildren = true;
					break;
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hasChildren;
	}
	
}
