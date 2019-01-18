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
		
		// On peut appeler une m�thode statique d'une classe
		// sans pour autant faire une instance de cette classe avant
		Connect connexion = MySQLConnect.getMySQLConnect();
		
		//MySQLConnect connexion = new MySQLConnect();
		//Connect connexion = new MySQLConnect();
		
		connexion.doConnect();
		
		// Acc�s aux attributs
		if (connexion.isAlive()) { // Etat de isAlive pour l'objet courant
			System.out.println("La connexion MySQL est op�rationnelle"); 
		} else {
			System.out.println("La connexion MySQL a �chou�"); 
		}
		connexion.doDisconnect();
		
		// Testons la surcharge de la m�thode doConnect
		System.out.println(((MySQLConnect)connexion).doConnect(true));
		
		// Acc�s aux attributs
		if (connexion.isAlive()) { // Etat de isAlive pour l'objet courant
			System.out.println("La connexion MySQL est op�rationnelle"); 
		} else {
			System.out.println("La connexion MySQL a �chou�"); 
		}
		connexion.doDisconnect();
		
		// Test avec un constructeur diff�rent
		Connect otherCnx = new MySQLConnect(true);
	}

}
