package com.crm.models;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.crm.models.interfaces.CheckClass;

/**
 * 
 * @author Aélion
 * 	Définition des sociétés
 *
 */

@Table(name="companies")

public class CompanyModel extends Model implements CheckClass {
	protected String name;
	protected String address;
	protected String zipcode;
	protected String city;
	
	protected ArrayList<ContactModel> contacts = new ArrayList<ContactModel>();
	// Setters simplifiés
	
	/**
	 * Le constructeur parent (super) se charge de me calculer
	 * automatiquement la valeur de entityName
	 * @param repository
	 * @throws SQLException 
	 */
	public CompanyModel(Repository repository) throws SQLException {
		super(repository);
	}
	
	public CompanyModel() {}
	/**
	 * Définit l'identifiant de la société
	 * @param id
	 * @return CompanyModel
	 */
	public CompanyModel id(int id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Ajoute un contact à la liste des contacts de la compagnie
	 * @param ContactModel contact
	 * @return CompanyModel
	 */
	public CompanyModel addContact(ContactModel contact) {
		// TODO Imaginer une solution pour retourner le statut de l'ajout
		if (this.contacts.size() > 0 ) {
			if (!this.contacts.contains(contact)) {
				// TODO Utiliser le comparable pour éliminer les éventuels doublons
				boolean doAdd = true; // On part du principe qu'on peut ajouter le contact
				for (ContactModel aContact : this.contacts) {
					if (aContact.compareTo(contact) == 0) {
						doAdd = false;
						break;
					}
				}
				
				if (doAdd) {
					this.contacts.add(contact);
				}
			}
		} else {
			this.contacts.add(contact);
		}
		
		return this;
	}
	
	/**
	 * @deprecated
	 * @since 2019-01-17
	 * @see CompanyModel id(int id)
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Définit le nom de la société
	 * @param name
	 * @return CompanyModel
	 */
	public CompanyModel name(String name) {
		this.name = name;
		return this;
	}
	
	public CompanyModel address(String address) {
		this.address = address;
		return this;
	}
	
	public CompanyModel zipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}
	
	public CompanyModel city(String city) {
		this.city = city;
		return this;
	}
	
	// Getters simplifiés
	public int id() {
		return this.id;
	}
	
	public String name() {
		return this.name;
	}
	
	public String address() {
		return this.address;
	}
	
	public String zipcode() {
		return this.zipcode;
	}
	
	public String city() {
		return this.city;
	}

	public CompanyModel hydrate(ResultSet row) throws NoSuchMethodException, SecurityException, SQLException {
		CompanyModel company = new CompanyModel();
		company.id(row.getInt(1));
		int indice = 2;
		for (Field field : this.getClass().getDeclaredFields()) {
			if (
				!field.getGenericType().getTypeName().contains("ArrayList") &&
				!field.getGenericType().getTypeName().contains("Model")
			) {
				Method method = this.getClass().getDeclaredMethod(field.getName(), field.getType());
				try {
					method.invoke(company, row.getString(indice));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				indice++;
			}
		}
		return company;
	}
	public String listContacts() {
		String list = "";
		// Boucle sur la liste des contacts
		for (ContactModel contact : this.contacts) {
			list += "Civilité : " + contact.civility() + "\n";
			list += "Nom : " + contact.name() + "\n";
			list += "Prénom : " + contact.forname() + "\n";
		}
		return list;
	}
	
	@Override
	public String check() {
		return "Nb. contacts : " + 
				this.contacts.size() + 
				"\n[Entité courante : " 
				+ this.entityName + "]";
	}
	
	public String toString() {
		return "Id : " + this.id + "\n" +
				"Nom : " + this.name + "\n" +
				"Ville : " + this.city;
	}
}
