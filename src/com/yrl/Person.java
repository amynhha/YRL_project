package com.yrl;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models the person: UUID, last name, first name, address, email(s).
 */
public class Person {

	private String uuid; 
	private String lastName;
	private String firstName;
	private Address address;
	private List<String> emails;
	
	public Person(String uuid, String lastName,  String firstName, Address address) {
		super();
		this.uuid = uuid;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.emails = new ArrayList<>();
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String nameToString() {
		return lastName + ", " + firstName;
	}

	@Override
	public String toString() {
		String toString = String.format(this.nameToString() + " (" + this.getUuid()+")\n\t");
		if(this.getEmails().size() == 1) {
			toString += "[" + this.getEmails().get(0) + "]\n";
		} else if(this.getEmails().size() > 1) {
			toString += "[";
			for(int i=0; i<this.getEmails().size()-1; i++) {
				toString += this.getEmails().get(i) + ", ";
			}
			toString += this.getEmails().get(this.getEmails().size()-1) + "]\n";
		} else if(this.getEmails().size() == 0) {
			toString += "\n";
		}
		toString += this.getAddress().toString();
		return toString;
		
	}
	
	public Address getAddress() {
		return address;
	}
	
	public List<String> getEmails() {
		return emails;
	}
	
	public void addEmail(String email) {
		this.emails.add(email);
	}
	
}
