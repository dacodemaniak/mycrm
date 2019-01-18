/**
 * 
 */
package com.test;

/**
 * @author jean-
 *
 */
public class RealPerson extends Person {

	/**
	 * 
	 */
	public RealPerson() {}

	/**
	 * @param name
	 */
	public RealPerson(String name) {
		super(name);
		this.name = this.name.toUpperCase();
	}

	/**
	 * @param civility
	 * @param forname
	 * @param name
	 */
	public RealPerson(String civility, String forname, String name) {
		super(civility, forname, name);
		this.name = this.name.toUpperCase();
	}
	
	@Override
	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	
	@Override
	public String identity() {
		return "Moi être humain "+ super.identity();
	}
}
