/**
 * 
 */
package com.crm.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.crm.dao.MySQLConnect;

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
		query.executeUpdate();
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.crm.models.ModelInterface#remove()
	 */
	@Override
	public boolean remove() {
		return this.repository.remove(this.id);
	}
	
	/**
	 * G�n�re une requ�te de type SELECT sur un mod�le
	 * @return String
	 */
	public String select() {
		return "SELECT " + this._getAttributes() + " FROM " + this.entityName + ";";
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
		
		System.out.println("Requ�te pr�par�e : " + query);
		
		return statement;
	}
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
						 
						OneToMany annotation = field.getAnnotation(OneToMany.class);
						
						allColumns += annotation.name() + "_id,";
						*/
						
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
		System.out.println("A la fin ? " + allColumns);
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
	
}
