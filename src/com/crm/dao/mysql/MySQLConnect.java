/**
 * 
 */
package com.crm.dao.mysql;

import com.crm.dao.Connect;
import com.crm.dao.DbParams;

/**
 * @author jean-
 *
 */
public class MySQLConnect extends Connect {

	/**
	 * Constructeur de la classe MySQLConnect
	 * 	Initialise un certain nombre de propriétés
	 * 	Peut appeler des méthodes
	 * 	...
	 */
	private MySQLConnect() {
		this.isAlive = false;
		this.doConnect();
	}
	
	private MySQLConnect(boolean forTesting) {
		this.isAlive = forTesting;
		this.doConnect(forTesting);
	}
	
	public static final Connect getMySQLConnect() {
		Connect connexion = new MySQLConnect();
		
		return connexion;
	}
	
	/* (non-Javadoc)
	 * @see com.crm.dao.Connect#doConnect()
	 */
	
	/**
	 * Surcharge de la méthode
	 * @param asString
	 * @return String
	 */
	public String doConnect(boolean asString) {
		String dsn = DbParams.SERVER + ";" + DbParams.USER + ";" +
				DbParams.PASSWORD;
		
		this.isAlive = false;
		
		return dsn;
	}
	
	@Override
	public void doConnect() {
		this.isAlive = true;
	}
	
	@Override
	public void doDisconnect() {
		System.out.println("Au revoir MySQL");
	}
}
