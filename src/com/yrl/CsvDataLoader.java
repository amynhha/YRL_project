package com.yrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Authors: Amy Nguyen and Naomi Post 
 * Date: 2024-03-08 
 * This class contains methods to load data from CSV files according to type.
 */
public class CsvDataLoader {

	/**
	 * Loads up a collection of Persons from the given file.
	 * 
	 * @param filePath
	 * @return personMap
	 * 
	 */
	public static Map<String, Person> loadPersonData(String filePath) {
		if (filePath == null) {
			return null;
		}

		Map<String, Person> personMap = new HashMap<>();
		File f = new File(filePath);
		Scanner s;
		try {
			s = new Scanner(f);
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String uuid = tokens[0];
				String firstName = tokens[1];
				String lastName = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];
				Address address = new Address(street, city, state, zip);
				Person person = new Person(uuid, lastName, firstName, address);
				String email;
				if (tokens.length > 7) {
					for (int i = 7; i < tokens.length; i++) {
						email = tokens[i];
						person.addEmail(email);
					}
				}
				personMap.put(uuid, person);
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchElementException nsee) {
			System.out.print("No data in file");
		}
		return personMap;
	}

	/**
	 * Loads up a collection of Items from the given file.
	 *
	 * @param filePath
	 * @return itemMap
	 * 
	 */
	public static Map<String, Item> loadItemData(String filePath) {
		if (filePath == null) {
			return null;
		}

		Map<String, Item> itemMap = new HashMap<>();
		File f = new File(filePath);
		Scanner s;
		try {
			s = new Scanner(f);
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String code = tokens[0];
				String itemType = tokens[1];
				String name = tokens[2];
				double basePrice = Double.parseDouble(tokens[3]);
				if (itemType.equals("V")) {
					Voice item = new Voice(code, name, basePrice);
					itemMap.put(code, item);
				} else if (itemType.equals("D")) {
					Data item = new Data(code, name, basePrice);
					itemMap.put(code, item);
				} else if (itemType.equals("S")) {
					Service item = new Service(code, name, basePrice);
					itemMap.put(code, item);
				} else if (itemType.equals("P")) {
					Product item = new Product(code, name, basePrice);
					itemMap.put(code, item);
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchElementException nsee) {
			System.out.print("No data in file");
		}
		return itemMap;
	}

	/**
	 * Loads up a collection of Sales from the given file.
	 *
	 * @param filePath, storeMap, personMap
	 * @return saleMap
	 * 
	 */
	public static Map<String, Sale> loadSaleData(String filePath, Map<String, Store> storeMap, Map<String, Person> personMap) {
		if (filePath == null) {
			return null;
		}

		Map<String, Sale> saleMap = new HashMap<>();
		File f = new File(filePath);
		Scanner s;
		try {
			s = new Scanner(f);
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				if (tokens.length == 5) {
					String saleCode = tokens[0];
					String storeCode = tokens[1];
					String customerUuid = tokens[2];
					String salesPersonUuid = tokens[3];
					LocalDate date = LocalDate.parse(tokens[4]);

					if (personMap.containsKey(customerUuid) && personMap.containsKey(salesPersonUuid)) {
						Sale sale = new Sale(saleCode, storeMap.get(storeCode), personMap.get(customerUuid),
								personMap.get(salesPersonUuid), date);
						if (!saleMap.containsValue(sale)) {
							saleMap.put(saleCode, sale);
						}
					} else {
						s.close();
						throw new RuntimeException("Not found");
					}
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchElementException nsee) {
			System.out.print("No data in file");
		} catch (IndexOutOfBoundsException ioob) {
			System.out.print("Out of bounds in SalesData\n");
		}
		return saleMap;
	}

	/**
	 * Loads up a collection of Sales Items from the given file.
	 *
	 * @param filePath, itemMap, saleMap, personMap
	 * @return salesItemMap
	 *
	 */
	public static Map<String, Item> loadSalesItem(String filePath, Map<String, Item> itemMap, Map<String, Sale> saleMap,
			Map<String, Person> personMap) {
		if (filePath == null) {
			return null;
		}

		Map<String, Item> salesItemMap = new HashMap<>();
		File f = new File(filePath);
		Scanner s;
		try {
			s = new Scanner(f);
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				String rawTokens[] = line.split(",");

				List<String> nonEmptyTokens = new ArrayList<String>();
				for (String str : rawTokens) {
					if (!str.isEmpty()) {
						nonEmptyTokens.add(str);
					}
				}
				String[] tokens = nonEmptyTokens.toArray(new String[0]);

				String saleCode = tokens[0];
				if (tokens.length > 1) {
					String itemCode = tokens[1];
					Item a = null;

					for (Item item : itemMap.values()) {
						if (itemCode.equals(item.getCode())) {
							if (item instanceof Product && tokens.length == 2) {
								a = new Purchase(item.getCode(), item.getName(), ((Product) item).getBasePrice());
								salesItemMap.put(saleCode, a);

							} else if (item instanceof Product && tokens.length == 4) {
								a = new Lease(item.getCode(), item.getName(), ((Product) item).getBasePrice(),
										LocalDate.parse(tokens[2]), LocalDate.parse(tokens[3]));
								salesItemMap.put(saleCode, a);

							} else if (item instanceof Service) {
								String servicerUuid = tokens[3];
								if (personMap.containsKey(servicerUuid)) {
									a = new Service(item.getCode(), item.getName(), ((Service) item).getHourlyPrice(),
											Double.parseDouble(tokens[2]), personMap.get(servicerUuid));
									salesItemMap.put(saleCode, a);

								} else {
									s.close();
									throw new RuntimeException("Not found");
								}

							} else if (item instanceof Data) {
								double numGBs = Double.parseDouble(tokens[2]);
								a = new Data(item.getCode(), item.getName(), ((Data) item).getPricePerGB(), numGBs);
								salesItemMap.put(saleCode, a);

							} else if (item instanceof Voice) {
								String phoneNum = tokens[2];
								int numDays = Integer.parseInt(tokens[3]);
								a = new Voice(item.getCode(), item.getName(), ((Voice) item).getPricePerPeriod(),
										phoneNum, numDays);
								salesItemMap.put(saleCode, a);

							}
							break;
						}
					}

					if (saleMap.containsKey(saleCode)) {
						saleMap.get(saleCode).addSale(a);
					} else {
						System.out.println("Sale not found");
					}
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchElementException nsee) {
			System.out.print("No data in file");
		} catch (IndexOutOfBoundsException ioob) {
		}
		return salesItemMap;
	}

	/**
	 * Loads up a collection of Stores from the given file.
	 *
	 * @param filePath, personList
	 * @return storeMap
	 * 
	 */
	public static Map<String, Store> loadStoreData(String filePath, Map<String, Person> personMap) {
		if (filePath == null) {
			return null;
		}

		Map<String, Store> storeMap = new HashMap<>();
		File f = new File(filePath);
		Scanner s;
		try {
			s = new Scanner(f);
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				if (tokens.length == 6) {
					String storeCode = tokens[0];
					String managerUuid = tokens[1];
					String street = tokens[2];
					String city = tokens[3];
					String state = tokens[4];
					String zip = tokens[5];
					Address address = new Address(street, city, state, zip);

					if (personMap.containsKey(managerUuid)) {
						Store store = new Store(storeCode, personMap.get(managerUuid), address);
						storeMap.put(storeCode, store);

					} else {
						Person manager = new Person(managerUuid, null, null, null);
						Store store = new Store(storeCode, manager, address);
						storeMap.put(storeCode, store);
					}
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchElementException nsee) {
			System.out.print("No data in file");
		} catch (IndexOutOfBoundsException ioob) {
			System.out.print("Out of bounds in StoreData\n");
		}
		return storeMap;
	}

}