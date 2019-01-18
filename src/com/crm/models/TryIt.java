/**
 * 
 */
package com.crm.models;

/**
 * @author jean-
 *
 */
public class TryIt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Repository companyRepository = new Repository();
		
		// Tentative d'instanciation d'une compagnie
		CompanyModel aelion = new CompanyModel(companyRepository)
				.id(1)
				.address("95, chemin de Gabardie")
				.city("Toulouse")
				.zipcode("31000")
				.name("Aélion");
		// Tester la persistence
		aelion.persist();
		
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
		worldCompany.persist();
		try {
			System.out.println(worldCompany.insert());
		} catch(Exception e) {
			System.out.println("An error occured " + e.getMessage());
		}
		
		// INSERT INTO companys (id, address, city, zipcode, name)
		// VALUES (2, '21 Jump Street', 'New-York', '00001', 'World Company');
		
		// Tester le find...
		System.out.println("Société créée : " + companyRepository.find(1).name());
		
		Repository contactRepository = new Repository();
		ContactModel moi = new ContactModel(contactRepository)
				.name("Aubert")
				.forname("Jean-Luc")
				.civility("Mr");
		
		aelion.addContact(moi);
		moi.persist();
		
		
		try {
			System.out.println(moi.insert());
		} catch(Exception e) {
			System.out.println("An error occured : " + e.getMessage());
		}
		
		// Tester un SELECT sur les compagnies
		System.out.println(moi.select());
		
		ContactModel lui = new ContactModel(contactRepository)
			.name("Aubert")
			.forname("Clélie")
			.civility("Mlle");
		
		aelion.addContact(lui);
		lui.persist();
		
		if (moi.compareTo(lui) == 0) {
			System.out.println("A priori, ce sont les mêmes noms");
		}
		
		System.out.println(aelion.check() + " " + moi.check());

	}

}
