/**
 * 
 */
package com.crm.models;

/**
 * @author jean-
 *
 */
public class TestModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Repository contacts = new Repository();
		
		ContactModel contact = new ContactModel(contacts)
			.name("Aubert")
			.forname("Jean-Luc")
			.civility("Monsieur")
			.id(1);
		// Fait persister le contact dans le repository des contacts
		((Model) contact).persist();
		
		contact = new ContactModel(contacts)
				.name("Mélanie")
				.forname("Zetaufrais")
				.civility("Mademoiselle")
				.id(2);
		// Fait persister le contact dans le repository des contacts
		((Model) contact).persist();
		
		// Afficher la liste des contacts
		System.out.println(contacts);
		
		// Chercher un contact par son id
		Model melanie = contacts.find(2);
		System.out.println((ContactModel) melanie);
		
		
	}

}
