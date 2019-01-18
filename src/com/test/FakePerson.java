/**
 * 
 */
package com.test;

/**
 * @author jean-
 *
 */
public class FakePerson extends Person {

	/**
	 * 
	 */
	public FakePerson() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public FakePerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param civility
	 * @param forname
	 * @param name
	 */
	public FakePerson(String civility, String forname, String name) {
		super(civility, forname, name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String identity() {
		return "Fantasy " + super.identity();
	}

}
