package com.yrl;

/**
 * This class models a Service plan Item.
 */
public class Service extends Item {

	private double numHours;
	private Person servicer;
	final private double TAX_RATE = .035;
	private double hourlyPrice;

	public Service(String code, String name, double hourlyPrice, double numHours, Person servicer) {
		super(code, name);
		this.numHours = numHours;
		this.servicer = servicer;
		this.hourlyPrice = hourlyPrice;
	}

	
	public Service(String code, String name, double hourlyPrice) {
		super(code, name);
		this.hourlyPrice = hourlyPrice;
	}

	public double getHourlyPrice() {
		return this.hourlyPrice;
	}

	public double getNumHours() {
		return numHours;
	}

	public Person getServicer() {
		return servicer;
	}
	
	@Override
	public String toString() {
		String toString = String.format("%s (%s) - Served by %s\n\t%.2f hours @ $%.2f/hour",
				this.getName(), this.getCode(), this.getServicer().nameToString(), 
				this.getNumHours(), this.getHourlyPrice());
		return toString;
	}
	
	/**
	 * This method gets the total values before tax based on the number of hours of the service. 
	 */
	public double getSubTotal() {
		double subTotal = this.getHourlyPrice() * this.getNumHours();
		return Math.round(subTotal * 100) / 100.0;
	}
	
	/**
	 * This method gets the tax. 
	 */
	public double getTaxes() {
		return Math.round(this.getSubTotal() * TAX_RATE * 100) / 100.0;
	}
	
	/**
	 * This method gets the total values after tax. 
	 */
	public double getGrandTotal() {
		return this.getSubTotal() + this.getTaxes();
	}
}
