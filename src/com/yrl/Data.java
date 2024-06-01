package com.yrl;

/**
 * This class models the Data Item.
 */
public class Data extends Item {

	private double numGBs;
	final private double TAX_RATE = .055;
	private double pricePerGB;

	public Data(String code, String name, double pricePerGB, double numGBs) {
		super(code, name);
		this.pricePerGB = pricePerGB;
		this.numGBs = numGBs;
	}

	public Data(String code, String name, double pricePerGB) {
		super(code, name);
		this.pricePerGB = pricePerGB;
	}

	public double getNumGBs() {
		return this.numGBs;
	}
	
	public double getPricePerGB() {
		return this.pricePerGB;
	}
	
	@Override
	public String toString() {
		String toString = String.format("%s (%s) - Data\n\t%.2f GB @ $%.2f/GB",this.getName(),this.getCode(),this.getPricePerGB(),this.getNumGBs());
		return toString;
	}
	
	/**
	 * This method gets the total values before tax based on amount of GBs purchased. 
	 */
	@Override
	public double getSubTotal() {
		double subTotal = this.getNumGBs() * this.getPricePerGB();
		return Math.round(subTotal * 100) /100.0;
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
