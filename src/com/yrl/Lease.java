package com.yrl;

import java.time.LocalDate;
import java.time.Period;

/**
 * This class models the Lease Product.
 */
public class Lease extends Product {

	private double markup = 1.5;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public Lease(String code, String name, double basePrice, LocalDate startDate, LocalDate endDate) {
		super(code, name, basePrice);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getStartDate() {
		return this.startDate.toString();
	}

	public String getEndDate() {
		return this.endDate.toString();
	}
	
	/**
	 * This method calculates the months between 2 specific dates.
	 * @return months
	 */
    public int getTime() {
    	int months = Period.between(this.startDate, this.endDate).getYears() * 12 + Period.between(this.startDate, this.endDate).getMonths();
		return months;
    }
    
    @Override
	public String toString() {
		return super.getName() + " (" + super.getCode() +") - Lease for " + this.getTime() + " months";
	}
    
	/**
	 * This method gets the monthly total values before tax based on a 50% mark up. 
	 */
    @Override
	public double getSubTotal() {
    	double subTotal = this.getBasePrice() * markup / this.getTime();
		return Math.round(subTotal * 100) / 100.0;
	}
    
	/**
	 * This method gets the tax. 
	 */
    @Override 
    public double getTaxes() {
    	return 0.0;
    }
    
	/**
	 * This method gets the total values after tax. 
	 */
	@Override
	public double getGrandTotal() {
		return this.getSubTotal() + this.getTaxes();
	}

}
