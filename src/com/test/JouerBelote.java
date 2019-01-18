/**
 * 
 */
package com.test;

import java.util.ArrayList;

/**
 * @author jean-
 *
 */
public class JouerBelote {
	private ArrayList<Person> players;
	
	public JouerBelote() {
		this.players = new ArrayList<Person>();
	}
	
	public void addPlayer(Person player) {
		if (!this.players.contains(player)) {
			this.players.add(player);
		}
	}
	
	public void removePlayer(Person player) {
		this.players.remove(player);
	}
	
	public Validation doIPlay() {
		if (this.players.size() >= 4) {
			// On vérifie si on a au moins 4 RealPerson
			int nbRealPerson = 0;
			
			for (Person player : this.players) {
				if (player instanceof RealPerson) {
					nbRealPerson++;
					// nbRealPerson = nbRealPerson + 1;
				}
			}
			
			// Ici, début du contrôle
			// return nbRealPerson >= 4 ? true : false;
			// Fin du contrôle
			
			// En fin de contrôle...
			if (nbRealPerson >= 4) {
				return new Validation(Validation.LETS_PLAY, "C'est bon (ou presque), on peut démarrer la partie");
			} else {
				// J'ai assez de personnes, mais pas assez d'humains
				Validation validation = new Validation(Validation.TO_FEW_REAL_PLAYERS, "Pas assez de joueurs humains");
				
				return validation;
			}
		}
		
		// L'objet Validation doit déterminer que la partie
		// ne peut pas commencer car je n'ai pas assez de joueurs
		Validation validation = new Validation(Validation.TO_FEW_PLAYERS, "Nombre de joueurs insuffisants");
		return validation;
	}
}
