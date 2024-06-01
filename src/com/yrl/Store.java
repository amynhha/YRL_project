package com.yrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class models the store: store code, manager's UUID, address, and a list of sales.
 */
public class Store {

	private String storeCode;
	private Person manager;
	private Address address;
	private List<Sale> sales = new ArrayList<>();
	
	public Store(String storeCode, Person manager, Address address) {
		super();
		this.storeCode = storeCode;
		this.manager = manager;
		this.address = address;
	}
	
	public String getStoreCode() {
		return storeCode;
	}
	
	public Person getManager() {
		return manager;
	}
	
	public Address getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return Objects.hash(storeCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		return Objects.equals(storeCode, other.storeCode);
	}
	
	public void addSale(Sale s) {
		this.sales.add(s);
	}
}
