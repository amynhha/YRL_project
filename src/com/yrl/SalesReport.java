package com.yrl;

import java.util.Comparator;
import java.util.Map;

/**
 * Authors: Amy Nguyen and Naomi Post
 * Date: 2024-04-03
 * This program prints the 3 reports.
 */
public class SalesReport {

	public static void main(String[] args) {

		Map<String, Person> personMap = DatabaseLoader.loadPersons();
		Map<String, Store> storeMap = DatabaseLoader.loadStores();
		Map<String, Item> itemMap = DatabaseLoader.loadItems();
		Map<String, Sale> saleMap = DatabaseLoader.loadSales();
		Map<String, Item> saleItemMap = DatabaseLoader.loadSalesItems(saleMap);
		
		LinkedList<Sale> salesLinkedListCustomer = new LinkedList<Sale>();
		for(Sale sale : saleMap.values()) {
			salesLinkedListCustomer.add(sale);
		}
		
		LinkedList<Sale> salesLinkedListValue = new LinkedList<Sale>();
		for(Sale sale : saleMap.values()) {
			salesLinkedListValue.add(sale);
		}
		
		LinkedList<Sale> salesLinkedListStore = new LinkedList<Sale>();
		for(Sale sale : saleMap.values()) {
			salesLinkedListStore.add(sale);
		}

		ReportUtils.printSalesCustomer(salesLinkedListCustomer);
		ReportUtils.printSalesValue(salesLinkedListValue);
		ReportUtils.printSalesStore(salesLinkedListStore);
	}


}

