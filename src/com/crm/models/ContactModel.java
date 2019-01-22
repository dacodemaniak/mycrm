/**
 * 
 */
package com.crm.models;

import java.sql.SQLException;

import com.crm.models.interfaces.CheckClass;

/**
 * @author Aélion
 * @version 1.0.0
 */
@Table(name="contacts")
public class ContactModel extends Model implements CheckClass, Comparable<ContactModel> {
	
	
	protected String name;
	
	protected String forname;
	
	protected String civility;
	
	@OneToMany(name="companies")
	protected CompanyModel company;
	
	public ContactModel(Repository repository) throws SQLException {
		super(repository);
	}
	
	// Getters and setters
	public ContactModel id(int id) {
		this.id= id;

		return this;
	}
	
	public ContactModel name(String name) {
		this.name = name;
		return this;
	}
	
	public ContactModel forname(String forname) {
		this.forname = forname;
		return this;
	}
	
	public ContactModel civility(String civility) {
		this.civility = civility;
		return this;
	}
	
	public ContactModel company(CompanyModel company) {
		this.company = company;
		return this;
	}
	
	public int id() {
		return this.id;
	}
	
	public String name() {
		return this.name;
	}
	
	public String forname() {
		return this.forname;
	}
	
	public String civility() {
		return this.civility;
	}
	
	public CompanyModel company() {
		return this.company;
	}
	
	public String toString() {
		return "[" + this.id + "] " + this.name + " " + this.forname;
	}

	@Override
	public String check() {
		return this.toString();
	}

	@Override
	public int compareTo(ContactModel anotherContact) {
		if (
			anotherContact.name() == this.name &&
			anotherContact.forname() == this.forname &&
			anotherContact.civility() == this.civility
		) {
			return 0;
		}
		return -1;
	}
	
}
