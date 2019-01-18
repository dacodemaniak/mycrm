/**
 * 
 */
package com.test;

import com.crm.dao.mysql.MySQLConnect;

/**
 * @author jean-
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Instancier une première personne
		RealPerson moi = new RealPerson("Aubert");
		moi.setForname("Jean-Luc");
		moi.setCivility("Mr");
		
		RealPerson lautre = new RealPerson(
			"Mme",
			"Mélanie",
			"Zetaufré"
		);
		
		FakePerson toi = new FakePerson("Le Fantôme");
		toi.setForname("Casper");
		toi.setCivility("Unknown");
		
		FakePerson lui = new FakePerson();
		lui.setName("Bond");
		lui.setForname("James");
		lui.setCivility("Mr");
		
		RealPerson desperateMin = new RealPerson();
		desperateMin.setName("Mathie");
		desperateMin.setForname("Mimi");
		desperateMin.setCivility("Mme");
		
		System.out.println(
			moi.identity() + " dit bonjour à " + 
			toi.identity() + "\n" +
			lui.identity() + " les rejoint\nUn de plus et on peut jouer."
		);
		
		System.out.println("Et moi ??? " + desperateMin.identity());
		
		JouerBelote partie = new JouerBelote();
		// Ajouter un joueur
		partie.addPlayer(moi);
		partie.addPlayer(toi);
		partie.addPlayer(lui);
		partie.addPlayer(new RealPerson("Mlle", "Mélusine", "Enfayitt"));
		partie.addPlayer(new RealPerson("Mr", "Jean", "Talut"));
		partie.addPlayer(desperateMin);
		
		// On peut jouer ?
		Validation validation = partie.doIPlay();
		System.out.println(validation);
		
		if (validation.getCode() >= 1) {
			System.out.println(validation.getReason());
		} else {
			System.out.println(validation.getReason());
		}
	}

}
