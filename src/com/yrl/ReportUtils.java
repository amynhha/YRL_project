package com.yrl;

import java.util.Comparator;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * Authors: Amy Nguyen and Naomi Post
 * Date: 2024-03-08
 * This class contains methods to print the 3 reports: by sales, by store, each sale.
 */
public class ReportUtils {

	private static final Logger LOGGER = LogManager.getLogger(ReportUtils.class);
	
	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.WARN);
		LOGGER.info("Started...");
	}
	
	/**
	 * This method prints the total per sale.
	 * @param salesList
	 */
	public static void printSales(Map<String, Sale> salesMap) {
		if(salesMap == null) {
			return;
		}
		
		LOGGER.debug("DEBUG: Printing summary report");

		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Summary Report - By Total                                                              |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Invoice #  Store      Customer                       Num Items          Tax       Total");
		double overallTax = 0;
		double overallTotal = 0;
		int totalCounter = 0;
		for(Sale s : salesMap.values()) {
			System.out.printf("%s       %s     %-31s%d          $%10.2f $%10.2f\n", s.getSaleCode(), s.getStore().getStoreCode(), 
							       															  s.getCustomer().nameToString(), 
							       															  s.getNumItems(), s.saleTotalTax(), s.saleGrandTotal());
			overallTax += s.saleTotalTax();
			overallTotal += s.saleGrandTotal();
			totalCounter += s.getNumItems();
		}
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.printf("                                                     %d          $%10.2f $%10.2f\n", totalCounter, overallTax, overallTotal);
		
	}
	
	/**
	 * This method prints the total per store.
	 * @param storesList, salesList
	 */
	public static void printStore(Map<String, Store> storesMap, Map<String, Sale> salesMap) {
		if(storesMap == null) {
			return;
		}
		
		LOGGER.debug("DEBUG: Printing store sales summary report");
		
		System.out.println("+----------------------------------------------------------------+");
		System.out.println("| Store Sales Summary Report                                     |");
		System.out.println("+----------------------------------------------------------------+");
		System.out.println("Store      Manager                        # Sales    Grand Total  ");
		
		int totalSales = 0;
		double overallTotal = 0;
		int numSales = 0;
		double totalPerStore = 0;
		for(Store store : storesMap.values()) {
			numSales = 0;
			totalPerStore = 0;
			for(Sale sale : salesMap.values()) {
				if(sale.getStore().equals(store)) {
					totalPerStore += sale.saleGrandTotal();
					totalSales++;
					numSales++;
				}
			}
			overallTotal += totalPerStore;
			System.out.printf("%s     %-31s%-10d $%10.2f\n",store.getStoreCode(),store.getManager().nameToString(),numSales,totalPerStore);
			
		}
		System.out.println("+----------------------------------------------------------------+");
		System.out.printf("                                          %-11d$%10.2f\n",totalSales,overallTotal);
	}	
	
	/**
	 * This method prints each sale.
	 * @param salesList
	 */
	public static void printIndivSale(Map<String, Sale> salesMap) {
		if(salesMap == null) {
			return;
		}

		LOGGER.debug("DEBUG: Printing sales summary report");

		for(Sale s : salesMap.values()) {
			System.out.printf("Sale     #%s\n",s.getSaleCode());
			System.out.printf("Store    #%s\n",s.getStore().getStoreCode());
			System.out.printf("Date      %s\n",s.getDate());
			System.out.println("Customer: ");
			System.out.println(s.getCustomer() + "\n");
			System.out.println("Sales Person: ");
			System.out.println(s.getSalesperson() + "\n");
			System.out.printf("Items (%d)                                                            Tax       Total\n", s.getNumItems());
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
		
		for(Item item : s.getItems()) {
			System.out.println(item);
			System.out.printf("                                                             $%10.2f $%10.2f\n", item.getTaxes(),item.getSubTotal());
			}
		
		System.out.println("                                                             -=-=-=-=-=- -=-=-=-=-=-");
		System.out.printf("                                                   Subtotals $%10.2f $%10.2f\n", s.saleTotalTax(),s.saleSubTotal());
		System.out.printf("                                                 Grand Total             $%10.2f\n", s.saleGrandTotal());
		}
	}
	
	/**
	 * This method prints all three of the above reports: by total per sale, by total per store,
	 * and each sale.
	 * @param personMap
	 * @param storeMap
	 * @param itemMap
	 * @param saleMap
	 * @param saleItemMap
	 */
	public static void printAllReports(Map<String,Person> personMap, Map<String,Store> storeMap, Map<String,Item> itemMap, 
									Map<String,Sale> saleMap, Map<String,Item> saleItemMap) {
		
		ReportUtils.printSales(saleMap);
		System.out.println();
		ReportUtils.printStore(storeMap, saleMap);
		System.out.println();
		ReportUtils.printIndivSale(saleMap);
	}

	/**
	 * This methods prints the report of sales that compares by customer
	 * @param <T>
	 * @param salesList
	 */
	public static <T> void printSalesCustomer(LinkedList<Sale> saleList) {
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Sales by Customer                                                                      |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total  ");
		LOGGER.debug("DEBUG: Printing sales summary by customer report");
		
		Comparator<Sale> cmpByCustomer = new Comparator<Sale>() {
			
			public int compare(Sale a, Sale b) {
				int customerComparison = a.getCustomer().getLastName().compareTo(b.getCustomer().getLastName());
				if(customerComparison == 0) {
					return a.getCustomer().getFirstName().compareTo(b.getCustomer().getFirstName());
				} else {
					return customerComparison;
				}
			}
		};
		
		LinkedList<Sale> salesLinkedListCustomer = new LinkedList<Sale>(cmpByCustomer);
		for(int i = 0; i < saleList.size(); i++) {
			Sale sale = saleList.get(i);
			salesLinkedListCustomer.add(sale);
		}
		for(int i = 0; i < salesLinkedListCustomer.size(); i++) {
			Sale s = salesLinkedListCustomer.get(i);
			System.out.printf("%-11s%-11s%-21s%-21s$%.2f\n", s.getSaleCode(), s.getStore().getStoreCode(), 
							       															  s.getCustomer().nameToString(), 
							       															  s.getSalesperson().nameToString(), 
							       															  s.saleGrandTotal());
		}
	}
	
	/**
	 * This methods prints the report of sales that compares by value
	 * @param <T>
	 * @param salesList
	 */
	public static <T> void printSalesValue(LinkedList<Sale> saleList) {
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Sales by Total                                                          |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total  ");
		LOGGER.debug("DEBUG: Printing sales summary by value report");
		
		Comparator<Sale> cmpByValue = new Comparator<Sale>() {
			
			public int compare(Sale a, Sale b) {
				if(a.saleGrandTotal() < b.saleGrandTotal()) {
					return 1;
				} else if(a.saleGrandTotal() > b.saleGrandTotal()) {
					return -1;
				} else {
					return 0;
				}
			}
		};
		
		LinkedList<Sale> salesLinkedListValue = new LinkedList<Sale>(cmpByValue);
		for(int i = 0; i < saleList.size(); i++) {
			Sale sale = saleList.get(i);
			salesLinkedListValue.add(sale);
		}
		for(int i = 0; i < salesLinkedListValue.size(); i++) {
			Sale s = salesLinkedListValue.get(i);
			System.out.printf("%-11s%-11s%-21s%-21s$%.2f\n", s.getSaleCode(), s.getStore().getStoreCode(), 
							       															  s.getCustomer().nameToString(), 
							       															  s.getSalesperson().nameToString(), 
							       															  s.saleGrandTotal());
		}
	}
	
	/**
	 * This methods prints the report of sales that compares by store
	 * @param <T>
	 * @param salesList
	 */
	public static <T> void printSalesStore(LinkedList<Sale> saleList) {
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Sales by Store                                                          |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total  ");
		LOGGER.debug("DEBUG: Printing sales summary by value report");
		
		Comparator<Sale> cmpBySalesPerson= new Comparator<Sale>() {
			
			public int compare(Sale a, Sale b) {
				int salesPersonComparison = a.getSalesperson().getLastName().compareTo(b.getSalesperson().getLastName());
				if(salesPersonComparison == 0) {
					return a.getSalesperson().getFirstName().compareTo(b.getSalesperson().getFirstName());
				} else {
					return salesPersonComparison;
				}
			}
		};
		Comparator<Sale> cmpByStore = new Comparator<Sale>() {
			
			public int compare(Sale a, Sale b) {
				int storeComparison = a.getStore().getStoreCode().compareTo(b.getStore().getStoreCode()) ;
				if(storeComparison == 0) {
					return cmpBySalesPerson.compare(a, b);
				} else {
					return storeComparison;
				}
			}
		};
		
		LinkedList<Sale> salesLinkedListStore = new LinkedList<Sale>(cmpByStore);
		for(int i = 0; i < saleList.size(); i++) {
			Sale sale = saleList.get(i);
			salesLinkedListStore.add(sale);
		}
		for(int i = 0; i < salesLinkedListStore.size(); i++) {
			Sale s = salesLinkedListStore.get(i);
			System.out.printf("%-11s%-11s%-21s%-21s$%.2f\n", s.getSaleCode(), s.getStore().getStoreCode(), 
							       															  s.getCustomer().nameToString(), 
							       															  s.getSalesperson().nameToString(), 
							       															  s.saleGrandTotal());
		}
	}
	
}
