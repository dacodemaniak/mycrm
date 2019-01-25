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
 * @author Aélion
 *
 */
public abstract class Model implements ModelInterface<Model> {

	/**
	 * @var String Nom de l'entité
	 */
	protected String entityName;
	
	/**
	 * @var int Identifiant d'une ligne du modèle
	 */
	protected int id;
	
	/**
	 * @var Repository Liste des objets de modèle
	 * Couplage faible (le repository continue à exister même si je détruis Model)
	 */
	protected Repository repository;
	
	/**
	 * Instance de connexion à la base de données
	 */
	protected Connection connexion;

	private Field field;
	
	protected PreparedStatement insert;
	
	/**
	 * Collectionner tous les attributs des classes de type ArrayList<Model>
	 */
	private ArrayList<Field> collections = new ArrayList<Field>();
	
	/**
	 * Instance de requête préparée :
	 * SELECT id FROM [entityName] ORDER BY id DESC LIMIT 0,1
	 */
	protected PreparedStatement lastId;
	
	protected PreparedStatement deleteStatement;
	
	/**
	 * Stratégie de suppression à adopter
	 */
	private DeleteStrategyInterface deleteStrategy;
	
	/**
	 * Constructeur avec DI (Dependency Injection / Injection de dépendance)
	 * @param repository
	 * @throws SQLException 
	 */
	public Model(Repository repository) throws SQLException {
		
		this.entityName = this._getEntityName();
		
		this.repository = repository;
		
		this.connexion = MySQLConnect.getConnexion();
		
		// Préparer toutes les requêtes...
		this.insert = this.preparedInsert();
		this.lastId = this.lastId();
		this.deleteStatement = this.preparedDelete();
		
		// Stratégie par défaut pour les suppressions : DeleteNothing
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
		String reflexion = "Nom de l'entité : " + this.entityName + "\n";
		
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
		
		// Utilisation des requêtes préparées
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
			indice++; // Ne pas oublier d'incrémenter le compteur...
		}
		// 2. Enfin... exécuter la requête finalisée
		//System.out.println("Requête : " + query.toString());
		query.executeUpdate();
		
		// 3. Dans tous les cas, on ne sait jamais, récupère le dernier id
		ResultSet res = this.lastId.executeQuery();
		
		if (res.first()) {
			//System.out.println("Récupère le dernier id");
			int lastId = res.getInt("id");

			// Ne pas oublier de définir l'id du modèle
			this.id = lastId;
			// Au passage, on ajoute au Repository
			this.repository.put(this);
			
			if (this.collections.size() > 0) {
				// So what ?
				for (Field field : this.collections) {
					//System.out.println("Boucle sur les collections");
					// Récupérer la valeur de ce field
					ArrayList<Model> values = (ArrayList<Model>) field.get(this);
					// Boucler sur les valeurs... et faire le job
					for (Model value : values) {
						// Récupérer la méthode concernée (celle qui va injecter CompanyModel dans ContactModel)
						ManyToOne method = value.getClass().getAnnotation(ManyToOne.class);
						
						//System.out.println("La méthode : "  + method.name());
						
						// Récupérer la méthode à partir de la classe
						try {
							Method manyToOne = value.getClass().getDeclaredMethod(method.name(), Model.class);
							// Invoquer la méthode en lui passant les paramètres éventuels
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
			
			// Hola, on vérifie avant qu'il n'y ait pas des enfants
			if (this.collections.size() == 0) {
				this.deleteStatement.executeUpdate();
				return true;				
			} else {
				// En fonction de la stratégie définie
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
	 * Génère une requête de type SELECT sur un modèle
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
		// Récupérer le nombre d'attributs et gérer les "placeholders"
		for (int i=0; i < this._getAttributesNumber(); i++) {
			query += "?,";
		}
		// Ne pas oublier de supprimer la dernière virgule en trop
		query = query.substring(0, query.length()-1);
		
		// A la fin, on termine la requête
		query += ");";
		
		PreparedStatement statement = this.connexion.prepareStatement(query);
		
		//System.out.println("Requête préparée : " + query);
		
		return statement;
	}
	
	private PreparedStatement lastId() {
		String query = "SELECT id FROM " + this.entityName + " ORDER BY id DESC LIMIT 0,1";
		try {
			return this.connexion.prepareStatement(query);
		} catch(SQLException e) {
			System.out.println("Erreur de préparation de requête : " + query + "[" + e.getMessage() + "]");
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
	 * Retourne le nom de la table à partir de l'annotation
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
					// Traiter spécifiquement un champ de type Model
					try {
						/**
						 * Way 1 :  passer par une annotation
						*/
						OneToMany annotation = field.getAnnotation(OneToMany.class);
						
						allColumns += annotation.name() + "_id,";
						
						/**
						// Way 2 : reflexion sur l'objet
						Class<? extends Model> parentClass = (Class<? extends Model>) field.getType();
						// On peut accéder aux annotations ?
						Table parentAnnotation = parentClass.getAnnotation(Table.class);
						allColumns += parentAnnotation.name() + "_id,";
						
						/* Way 3 : on se débrouille... comme on peut
						try {
							System.out.println("Entité de la classe parente : " + ((Model) field.get(this)).entityName);
							allColumns += ((Model) field.get(this)).entityName + "_id";
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						// Way 4 : truc alambiqué
						
					} catch(IllegalArgumentException e) {
						System.out.println("Argument illégal : " + e.getMessage());
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
				// L'ajouter à la collection
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
