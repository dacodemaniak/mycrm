/**
 * 
 */
package com.crm.dao;

/**
 * @author A�lion
 *
 */
public abstract class Connect {
	
	protected boolean isAlive;
	
	protected String connectType;
	
	public String connectType() {
		return this.connectType;
	}
	
	public Connect connectType(String type) {
		this.connectType = type;
		return this;
	}
	
	/**
	 * @return the connectType
	 */
	public String getConnectType() {
		return connectType;
	}

	/**
	 * @param connectType the connectType to set
	 */
	public Connect setConnectType(String connectType) {
		this.connectType = connectType;
		return this;
	}

	/**
	 * G�re la connexion proprement dite
	 * @return void
	 */
	public abstract void doConnect();
	
	public abstract void doDisconnect();
	
	public boolean isAlive() {
		return this.isAlive;
	}
}
