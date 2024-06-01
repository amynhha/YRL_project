package com.yrl;

/**
 * This class models the Purchase Product.
 */
public class Purchase extends Product {

	final private double TAX_RATE = .065;
	
	public Purchase(String code, String name, double basePrice) {
		super(code, name, basePrice);
	}
	
	@Override
	public String toString() {
		return super.getName() + " (" + super.getCode() + ")";
	}
	
	/**
	 * This method gets the total values before tax. 
	 */
	@Override
	public double getSubTotal() {
		return this.getBasePrice();
	}
	
	/**
	 * This method gets the tax
	 */
	@Override
	public double getTaxes() {
		return Math.round(this.getBasePrice() * TAX_RATE * 100) / 100.0;
	}
	
	/**
	 * This method gets the total values after tax. 
	 */
	@Override
	public double getGrandTotal() {
		return this.getSubTotal() + this.getTaxes();
	}
	
}
