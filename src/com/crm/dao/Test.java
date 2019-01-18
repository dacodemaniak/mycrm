/**
 * 
 */
package com.crm.dao;

import com.crm.dao.mysql.MySQLConnect;
import com.crm.dao.oracle.OracleConnect;

/**
 * @author jean-
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// On peut appeler une méthode statique d'une classe
		// sans pour autant faire une instance de cette classe avant
		Connect connexion = MySQLConnect.getMySQLConnect();
		
		//MySQLConnect connexion = new MySQLConnect();
		//Connect connexion = new MySQLConnect();
		
		connexion.doConnect();
		
		// Accès aux attributs
		if (connexion.isAlive()) { // Etat de isAlive pour l'objet courant
			System.out.println("La connexion MySQL est opérationnelle"); 
		} else {
			System.out.println("La connexion MySQL a échoué"); 
		}
		connexion.doDisconnect();
		
		// Testons la surcharge de la méthode doConnect
		System.out.println(((MySQLConnect)connexion).doConnect(true));
		
		// Accès aux attributs
		if (connexion.isAlive()) { // Etat de isAlive pour l'objet courant
			System.out.println("La connexion MySQL est opérationnelle"); 
		} else {
			System.out.println("La connexion MySQL a échoué"); 
		}
		connexion.doDisconnect();
		
		// Test avec un constructeur différent
		Connect otherCnx = new MySQLConnect(true);
	}

}
