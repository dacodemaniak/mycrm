package com.crm.dao.oracle;

import com.crm.dao.Connect;

public class OracleConnect extends Connect {
	
	public void doConnect() {
		System.out.println("Connexion � Oracle");
		this.isAlive = false;
	}
	
	public void doDisconnect() {
		System.out.println("D�connexion d'Oracle");
	}
}
