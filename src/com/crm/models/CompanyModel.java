package com.crm.models;

import java.util.ArrayList;

import com.crm.models.interfaces.CheckClass;

/**
 * 
 * @author A�lion
 * 	D�finition des soci�t�s
 *
 */
public class CompanyModel extends Model implements CheckClass {
	protected String name;
	protected String address;
	protected String zipcode;
	protected String city;
	
	protected ArrayList<ContactModel> contacts = new ArrayList<ContactModel>();
	// Setters simplifi�s
	
	/**
	 * Le constructeur parent (super) se charge de me calculer
	 * automatiquement la valeur de entityName
	 * @param repository
	 */
	public CompanyModel(Repository repository) {
		super(repository);
	}
	
	/**
	 * D�finit l'identifiant de la soci�t�
	 * @param id
	 * @return CompanyModel
	 */
	public CompanyModel id(int id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Ajoute un contact � la liste des contacts de la compagnie
	 * @param ContactModel contact
	 * @return CompanyModel
	 */
	public CompanyModel addContact(ContactModel contact) {
		// TODO Imaginer une solution pour retourner le statut de l'ajout
		if (this.contacts.size() > 0 ) {
			if (!this.contacts.contains(contact)) {
				// TODO Utiliser le comparable pour �liminer les �ventuels doublons
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
	 * D�finit le nom de la soci�t�
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
	
	// Getters simplifi�s
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

	@Override
	public String check() {
		return "Nb. contacts : " + 
				this.contacts.size() + 
				"\n[Entit� courante : " 
				+ this.entityName + "]";
	}
}
