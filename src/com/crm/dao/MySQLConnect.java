/**
 * 
 */
package com.crm.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * @author Aélion
 * @category Singleton
 *
 */
public class MySQLConnect {
	/**
	 * Connexion effective à la base de données
	 */
	private static Connection connection = null;
	
	// Constructeur pour établir la connexion au serveur de données
	private MySQLConnect() throws SQLException {
		// Définition des informations de connexion
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
	 * Méthode statique publique qui instancie si nécessaire
	 * @return Connection  Instance de connexion à la base de données
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
