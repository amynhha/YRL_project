package com.yrl;

/**
 * This class models the item: code and name.
 */
public abstract class Item implements Comparable<Item>{

	private String code;
	private String name;
	
	public Item(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * This method gets the total values before tax. 
	 */
	public abstract double getSubTotal();
	
	/**
	 * This method gets the tax. 
	 */
	public abstract double getTaxes();
	
	/**
	 * This method gets the total values after tax. 
	 */
	public abstract double getGrandTotal();
	
	@Override
	public int compareTo(Item other) {
		return getCode().compareTo(other.getCode());
	}
	
}
