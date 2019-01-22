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
		
		// Tester la connexion MySQL
		 // Singleton de connexion
		/**
		try {
			MySQLConnect connection = new MySQLConnect();
		} catch(SQLException e) {
			System.out.println("Une erreur est survenue : " + e.getMessage());
		}
		**/
		
		Repository companyRepository = new Repository();
		
		// Tentative d'instanciation d'une compagnie
		CompanyModel aelion = new CompanyModel(companyRepository)
				.id(1)
				.address("10, Place du Capitole")
				.city("Toulouse")
				.zipcode("31000")
				.name("Airbus'One");
		
		// INSERT INTO companys (id, address, city, zipcode, name)
		// VALUES (1, '95, chemin de Gabardie', 'Toulouse', '31000', 'Aélion');
		
		// Tester un SELECT sur les compagnies
		System.out.println(aelion.select());
		
		CompanyModel worldCompany = new CompanyModel(companyRepository)
				.id(2)
				.address("21 Jump Street")
				.city("New-York")
				.zipcode("00001")
				.name("World Company");
		
		// INSERT INTO companys (id, address, city, zipcode, name)
		// VALUES (2, '21 Jump Street', 'New-York', '00001', 'World Company');
		
		Repository contactRepository = new Repository();
		ContactModel moi = new ContactModel(contactRepository)
				.name("Aubert")
				.forname("Jean-Luc")
				.civility("Mr")
				.company(aelion);
		
		aelion.addContact(moi);
		
		ContactModel lui = new ContactModel(contactRepository)
			.name("Aubert")
			.forname("Clélie")
			.civility("Mlle")
			.company(worldCompany);
		
		worldCompany.addContact(lui);
		
		if (moi.compareTo(lui) == 0) {
			System.out.println("A priori, ce sont les mêmes noms");
		}
		
		System.out.println(aelion.check() + " " + moi.check());
		
		// A la fin...
		aelion.persist();
		worldCompany.persist();

	}

}
