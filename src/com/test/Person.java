/**
 * 
 */
package com.test;

/**
 * @author jean-
 *
 */
public class Person {
	protected String name;
	protected String forname;
	protected String civility;
	
	// Constructeur vide => objet vide => utiliser les setters
	public Person() {}
	
	// Constructeur avec un seule paramètre => objet avec un nom
	//	=> utiliser les setters pour forname et civility
	public Person(String name) {
		this.name = name;
	}
	
	// Constructeur avec 3 paramètres => construit un objet complet
	public Person(String civility, String forname, String name) {
		this.civility = civility;
		this.forname = forname;
		this.name = name;
	}
	
	public void setName(String name) {
		if (this.name == null) {
			this.name = name;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the forname
	 */
	public String getForname() {
		return this.forname;
	}

	/**
	 * @param forname the forname to set
	 */
	public void setForname(String forname) {
		this.forname = forname;
	}

	/**
	 * @return the civility
	 */
	public String getCivility() {
		return this.civility;
	}

	/**
	 * @param civility the civility to set
	 */
	public void setCivility(String civility) {
		this.civility = civility;
	}
	
	public String identity() {
		return this.civility + " " + this.forname + " " + this.name;
	}
}
