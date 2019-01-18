/**
 * 
 */
package com.crm.models;

import java.lang.reflect.Field;
import java.util.HashMap;

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
	 * Constructeur avec DI (Dependency Injection / Injection de d�pendance)
	 * @param repository
	 */
	public Model(Repository repository) {
		String name = 
				this.getClass().getName()
				.substring(0, this.getClass().getName().indexOf("Model"))
				.toLowerCase();
		String[] parts = name.split("\\.");
		this.entityName = parts[parts.length - 1] + "s";
		
		this.repository = repository;
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
	public Model persist() {
		return this.repository.put(this);
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
	
	public String insert() throws IllegalArgumentException, IllegalAccessException {
		return "INSERT INTO " + this.entityName + "(" + this._getAttributes() + ") VALUES (" +
				this._getValues() + ");";
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
			}
		}
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
				allValues += "'" + field.get(this).toString() + "',";
			}
		}
		return allValues.substring(0, allValues.length() - 1);		
	}
	
}
