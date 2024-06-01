package com.yrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class models the Sale.
 */
public class Sale {
	
	private String saleCode;
	private Store store;
	private Person customer;
	private Person salesPerson;
	private LocalDate date;
	private List<Item> itemsSold = new ArrayList<>();
		
	public Sale(String saleCode, Store store, Person customer, Person salesPerson, LocalDate date) {
		super();
		this.saleCode = saleCode;
		this.store = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.date = date;
		this.itemsSold = new ArrayList<>();
	}



	public Sale(String saleCode, List<Item> sales) {
		super();
		this.saleCode = saleCode;
		this.itemsSold = sales;
	}
	
	/**
	 * This method calculates the subTotal of all items in the Sale before tax. 
	 * @return totalPerSale
	 */
	public double saleSubTotal() {
		double totalPerSale = 0;
		for(Item item : this.getItems()) {
			totalPerSale += item.getSubTotal();
		}
		return totalPerSale;
	}
	
	/**
	 * This method calculates the taxes of all items in the Sale. 
	 * @return taxPerSale
	 */
	public double saleTotalTax() {
		double taxPerSale = 0;
		for(Item item : this.getItems()) {
			taxPerSale += item.getTaxes();
		}
		return taxPerSale;
	}
	
	/**
	 * This method calculates the grand total of all items in the Sale after tax. 
	 */
	public double saleGrandTotal() {
		return this.saleSubTotal() + this.saleTotalTax();
	}
	
	/**
	 * This method gets the total number of items for a Sale. 
	 */
	public int getNumItems() {
		return itemsSold.size();
	}

	public String getSaleCode() {
		return saleCode;
	}
	
	public Store getStore() {
		return store;
	}
	
	public Person getCustomer() {
		return customer;
	}
	
	public Person getSalesperson() {
		return salesPerson;
	}
	
    public String getDate() {
    	return this.date.toString();
    }
	
	public List<Item> getItems() {
		return itemsSold;
	}
	
	public void addSale(Item sale) {
		this.itemsSold.add(sale);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, itemsSold, saleCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sale other = (Sale) obj;
		return Objects.equals(date, other.date) && Objects.equals(itemsSold, other.itemsSold)
				&& Objects.equals(saleCode, other.saleCode);
	}
	
}
