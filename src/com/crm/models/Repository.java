/**
 * 
 */
package com.crm.models;

import java.util.HashMap;

/**
 * @author jean-
 *
 */
public class Repository {
	private HashMap<Integer, Model> repository = new HashMap<Integer, Model>();
	
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
	
	public boolean remove(int id) {
		if (this.repository.containsKey((Integer) id)) {
			this.repository.remove((Integer) id);
			return true;
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
}
