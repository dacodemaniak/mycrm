/**
 * 
 */
package com.crm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.crm.dao.MySQLConnect;
import com.crm.models.strategy.DeleteAll;

/**
 * @author jean-
 *
 */
public class TryIt {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, SQLException {
		
		Repository companyRepository = new Repository();
		
		// Tentative d'instanciation d'une compagnie
		CompanyModel aelion = new CompanyModel(companyRepository)
				.address("10, Place du Capitole")
				.city("Toulouse")
				.zipcode("31000")
				.name("Airbus'One");
	
		
		CompanyModel worldCompany = new CompanyModel(companyRepository)
				.address("21 Jump Street")
				.city("New-York")
				.zipcode("00001")
				.name("World Company");
		
		Repository contactRepository = new Repository();
		ContactModel moi = new ContactModel(contactRepository)
				.name("Aubert")
				.forname("Jean-Luc")
				.civility("Mr");
		
		aelion.addContact(moi);
		
		ContactModel lui = new ContactModel(contactRepository)
			.name("Aubert")
			.forname("Clélie")
			.civility("Mlle");
		
		worldCompany.addContact(lui);
		
		// A la fin...
		aelion.persist();
		worldCompany.persist();

		// On a quoi dans les repos ?
		System.out.print(contactRepository.toString());
		
		// On va essayer de supprimer un contact
		contactRepository.remove(lui); // Easy...
		
		// On essaye de supprimer une compagnie ?
		aelion.setDeleteStrategy(new DeleteAll());
		companyRepository.remove(aelion);
		
		CompanyModel acme = new CompanyModel(companyRepository)
				.name("Acme's")
				.address("Hollywood Bd")
				.zipcode("50000")
				.city("Los Angeles");
		acme.persist();
		// Puis je veux la supprimer
		companyRepository.remove(acme); // Pas de suppression si la stratégie est DeleteNothing
	}

}
