/**
 * 
 */
package com.ideafactory.dao;

/**
 * @author jean-
 *
 */
public class User {
	private String name;
	private String email;
	
	public User() {}
	
	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public User name(String name) {
		this.name = name;
		return this;
	}
	
	public String name() {
		return this.name;
	}
	
	public User email(String email) {
		this.email = email;
		return this;
	}
	
	public String email() {
		return this.email;
	}
}
