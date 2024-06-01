package com.yrl;

/**
 * This class models the address: street, city, state, and zip code.
 */
public class Address {

	private String street;
	private String city;
	private String state;
	private String zip;
	
	public Address(String street, String city, String state, String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
	
	@Override
	public String toString() {
		String toString = String.format("\t%s\n\t%s, %s %s",
										this.getStreet(), this.getCity(),this.getState(),this.getZip());
		return toString;
	}
}
