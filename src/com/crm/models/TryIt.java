/**
 * 
 */
package com.crm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.crm.dao.MySQLConnect;

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

		
	}

}
