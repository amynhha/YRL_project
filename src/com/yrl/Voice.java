package com.yrl;

/**
 * This class models a Voice plan Item.
 */
public class Voice extends Item {

	private int numDays;
	private String phoneNum;
	final private double TAX_RATE = .065;
	private double pricePerPeriod;

	public Voice(String code, String name, double pricePerPeriod, String phoneNum, int numDays) {
		super(code, name);
		this.phoneNum = phoneNum;
		this.numDays = numDays;
		this.pricePerPeriod = pricePerPeriod;
	}

	public Voice(String code, String name, double pricePerPeriod) {
		super(code, name);
		this.pricePerPeriod = pricePerPeriod;
	}
	
	public double getPricePerPeriod() {
		return this.pricePerPeriod;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}
	
	public int getNumDays() {
		return numDays;
	}
	
	@Override
	public String toString() {
		String toString = String.format("%s (%s) - Voice %s\n\t%d days @ $%.2f/30 day period",
										this.getName(),this.getCode(),this.getPhoneNum(),
										this.getNumDays(),this.getPricePerPeriod());
		return toString;
	}

	/**
	 * This method gets the total values before tax based on the number of days purchased. 
	 */
	@Override
	public double getSubTotal() {
		double subTotal = this.getPricePerPeriod() * ((double)this.getNumDays() / 30);
		return Math.round(subTotal * 100) / 100.0;
	}
	
	/**
	 * This method gets the tax. 
	 */
	@Override
	public double getTaxes() {
		return Math.round(this.getSubTotal() * TAX_RATE * 100) / 100.0;
	}
	
	/**
	 * This method gets the total values after tax. 
	 */
	@Override
	public double getGrandTotal() {
		return this.getSubTotal() + this.getTaxes();
	}
	
}
