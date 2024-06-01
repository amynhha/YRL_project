package com.yrl;

/**
 * This class models the Product Item, classifying a Lease or Purchase.
 */
public class Product extends Item {
	
	private double basePrice;

	public Product(String code, String name, double basePrice) {
		super(code, name);
		this.basePrice = basePrice;
	}
	
	public double getBasePrice() {
		return this.basePrice;
	}

	/**
	 * This method calculates the subTotal of all items in the Product before tax. 
	 */
	@Override
	public double getSubTotal() {
		return 0;
	}

	/**
	 * This method calculates the tax of the product. 
	 */
	@Override
	public double getTaxes() {
		return 0;
	}

	/**
	 * This method calculates the grandTotal of all items in the Product after tax. 
	 */
	@Override
	public double getGrandTotal() {
		return 0;
	}

}
