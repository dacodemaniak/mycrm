/**
 * 
 */
package com.crm.models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author jean-
 *
 */
public class Repository implements RepositoryInterface<Model>{
	private HashMap<Integer, Model> repository = new HashMap<Integer, Model>();
	private Model model;

	public void setModel(Model model) {
		this.model = model;
		this._hydrate();
	}
	
	public HashMap<Integer, Model> getRepository() {
		return this.repository;
	}
	
	/**
	 * Ajoute un élément de type Model à la liste des modèles
	 * @param id
	 * @param model
	 * @return Model
	 */
	public Model put(Model model) {
		this.repository.put((Integer) model.id, model);
		return model;
	}
	
	public Model put(int id, Model model) {
		this.repository.put((Integer) id, model);
		return model;
	}
	
	/**
	 * Retourne la valeur associée à une clé dans la liste des modèles
	 * @param id
	 * @return Model
	 */
	public Model find(int id) {
		return this.repository.containsKey((Integer) id) ? this.repository.get((Integer) id) : null;
	}
	
	/**
	 * Méthode de suppression à partir du Repository
	 * @param model
	 * @return boolean
	 */
	public boolean remove(Model model) {
		if (this.repository.containsValue(model)) {
			if (model.remove()) {
				// On dégage la ligne du HashMap
				this.repository.remove((Integer) model.id());
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		String output = "";
		
		for (Model model : this.repository.values()) {
			output += model.toString() + "\n";
		}
		return output;
	}
	
	private void _hydrate() {
		try {
			ResultSet rs = this.model.select();
			while(rs.next()) {
				this.put(this.model.hydrate(rs));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
