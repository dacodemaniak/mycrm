/**
 * 
 */
package com.crm.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * @author A�lion
 * @category Singleton
 *
 */
public class MySQLConnect {
	/**
	 * Connexion effective � la base de donn�es
	 */
	private static Connection connection = null;
	
	// Constructeur pour �tablir la connexion au serveur de donn�es
	private MySQLConnect() throws SQLException {
		// D�finition des informations de connexion
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl("jdbc:mysql://" + DbParams.SERVER + ":" + DbParams.PORT + "/" + DbParams.DB_NAME + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Paris"); // Adresse / Port du serveur mySQL
		dataSource.setUser(DbParams.USER);
		dataSource.setPassword(DbParams.PASSWORD);
		dataSource.setServerName(DbParams.SERVER);
		dataSource.setPortNumber(DbParams.PORT);
		dataSource.setDatabaseName(DbParams.DB_NAME);
		dataSource.setUseSSL(false);
		
		// Etablir la connexion proprement dite
		MySQLConnect.connection = dataSource.getConnection();
	}
	
	/**
	 * M�thode statique publique qui instancie si n�cessaire
	 * @return Connection  Instance de connexion � la base de donn�es
	 */
	public static Connection getConnexion() {
		if (MySQLConnect.connection == null) {
			try {
				System.out.println("Pas encore de connexion... Do it !");
				new MySQLConnect();
			} catch(SQLException e) {
				System.out.println("Erreur : " + e.getMessage());
			}
		}
		return MySQLConnect.connection;
	}
}
