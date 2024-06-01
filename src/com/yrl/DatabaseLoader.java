package com.yrl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * Authors: Amy Nguyen and Naomi Post 
 * Date: 2024-04-08 
 * This class contains methods to load the data in the database.
 */
public class DatabaseLoader {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);
	
	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.WARN);
		LOGGER.info("Started...");
	}
	
	/**
	 * This method loads an address from the database using the given {@code addressId}.
	 * If no address is found, returns null.
	 * 
	 * @param addressId
	 * @return address
	 */
	public static Address loadAddress(int addressId) {
		
		Address address = null;
		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to load an Address");

		conn = ConnectionFactory.getConnection();

		String query = "SELECT street, city, state, zip FROM Address where addressId = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, addressId);
			rs = ps.executeQuery();
			while (rs.next()) {

				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				address = new Address(street, city, state, zip);
			}

		} catch (SQLException e) {
			LOGGER.error("Could not load address with addressId = " + addressId, e);
			return null;
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}

		return address;
	}
	
	/**
	 * Loads up a collection of Persons from the database.
	 * 
	 * @return personMap
	 */
	public static Map<String, Person> loadPersons() {

		Map<String, Person> personMap = new HashMap<>();

		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to load persons");

		conn = ConnectionFactory.getConnection();

		String query = "SELECT personId, uuid, lastName, firstName, addressId FROM Person";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {

				String uuid = rs.getString("uuid");
				String lastName = rs.getString("lastName");
				String firstName = rs.getString("firstName");
				int personId = rs.getInt("personId");
				int addressId = rs.getInt("addressId");

				Address address = loadAddress(addressId);
				Person person = new Person(uuid, lastName, firstName, address);

				PreparedStatement psEmail = null;
				ResultSet rsEmail = null;

				String queryEmail = "SELECT email FROM Email WHERE personId = ?";
				psEmail = conn.prepareStatement(queryEmail);
				psEmail.setInt(1, personId);
				rsEmail = psEmail.executeQuery();
				while (rsEmail.next()) {
					String email = rsEmail.getString("email");
					person.addEmail(email);
				}
				rsEmail.close();
				psEmail.close();
				personMap.put(uuid, person);
			}

		} catch (SQLException e) {
			LOGGER.error("Could not load persons", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}

		return personMap;
	}

	/**
	 * Loads up a collection of Items from the database.
	 * 
	 * @return itemMap
	 */
	public static Map<String, Item> loadItems() {

		Map<String, Item> itemMap = new HashMap<>();

		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to load items");

		conn = ConnectionFactory.getConnection();

		String query = "SELECT i.code, i.type, i.name, i.basePrice FROM Item i";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {

				String code = rs.getString("i.code");
				String type = rs.getString("i.type");
				String name = rs.getString("i.name");
				double basePrice = rs.getDouble("i.basePrice");
				if (type.equals("V")) {
					Voice item = new Voice(code, name, basePrice);
					itemMap.put(code, item);
				} else if (type.equals("D")) {
					Data item = new Data(code, name, basePrice);
					itemMap.put(code, item);
				} else if (type.equals("S")) {
					Service item = new Service(code, name, basePrice);
					itemMap.put(code, item);
				} else if (type.equals("P")) {
					Product item = new Product(code, name, basePrice);
					itemMap.put(code, item);
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Could not load items", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}

		return itemMap;
	}

	/**
	 * Loads up a collection of Stores from the database.
	 * 
	 * @return storeMap
	 */
	public static Map<String, Store> loadStores() {

		Map<String, Store> storeMap = new HashMap<>();
		Map<String, Person> personMap = loadPersons();

		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to load stores");

		conn = ConnectionFactory.getConnection();

		String query = "SELECT st.storeId, st.storeCode, st.personId, st.addressId, p.uuid "
				+ "FROM Store st JOIN Person p ON p.personId = st.personId";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {

				int addressId = rs.getInt("st.addressId");
				Address address = loadAddress(addressId);
				String storeCode = rs.getString("st.storeCode");
				String personId = rs.getString("p.uuid");
				Person manager = personMap.get(personId);
				Store store = new Store(storeCode, manager, address);
				storeMap.put(storeCode, store);
			}
			rs.close();
			
		} catch (SQLException e) {
			LOGGER.error("Could not load stores", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return storeMap;
	}

	/**
	 * Loads up a collection of Sale from the database.
	 * 
	 * @return saleMap
	 */
	public static Map<String, Sale> loadSales() {
		
		Map<String, Sale> saleMap = new HashMap<>();
		Map<String, Store> storeMap = loadStores();
		Map<String, Person> personMap = loadPersons();
		
		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to load sales");

		conn = ConnectionFactory.getConnection();

		String query = "SELECT s.saleId, s.saleCode, s.date, st.storeCode, c.uuid, e.uuid FROM Sale s "
				+ "JOIN Store st ON s.storeId = st.storeId " + "JOIN Person c ON c.personId = s.customerId "
				+ "JOIN Person e ON e.personId = s.salesPersonId";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				String saleCode = rs.getString("s.saleCode");
				String storeCode = rs.getString("st.storeCode");
				String customerUuid = rs.getString("c.uuid");
				String salesPersonUuid = rs.getString("e.uuid");
				LocalDate date = LocalDate.parse(rs.getString("s.date"));
				
				Store store = storeMap.get(storeCode);
				Person customer = personMap.get(customerUuid);
				Person salesPerson = personMap.get(salesPersonUuid);
				Sale sale = new Sale(saleCode, store, customer, salesPerson, date);
				saleMap.put(saleCode, sale);
				
			}
			rs.close();
		} catch (SQLException e) {
			LOGGER.error("Could not load sales", e);
			throw new RuntimeException(e);
		}
		
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		
		return saleMap;
	}

	/**
	 * Loads up a collection of SalesItem from the database.
	 * 
	 * @return salesItemMap
	 */
	public static Map<String, Item> loadSalesItems(Map<String, Sale> saleMap) {

		Map<String, Item> salesItemMap = new HashMap<>();
		Map<String, Store> storeMap = loadStores();
		Map<String, Person> personMap = loadPersons();

		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to load sales items");

		conn = ConnectionFactory.getConnection();

		String query = "SELECT s.saleId, s.saleCode, s.date, st.storeCode, s.customerId, s.salesPersonId FROM Sale s "
				+ "LEFT JOIN Store st ON s.storeId = st.storeId " 
				+ "LEFT JOIN Person c ON c.personId = s.customerId "
				+ "LEFT JOIN Person e ON e.personId = s.salesPersonId";

		String salesItemQuery = "SELECT si.*, i.type, i.name, i.code as itemCode, i.basePrice, p.uuid as servicerCode FROM SaleItem si "
				+ "LEFT JOIN Item i ON si.itemId = i.itemId " + "LEFT JOIN Person p ON si.servicerId = p.personId "
				+ "WHERE si.saleId = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psSaleItem = null;
		ResultSet rsSaleItem = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {

				int saleId = rs.getInt("s.saleId");
				String saleCode = rs.getString("s.saleCode");
				String storeCode = rs.getString("st.storeCode");
				String customerId = rs.getString("s.customerId");
				String salesPersonId = rs.getString("s.salesPersonId");
				LocalDate date = LocalDate.parse(rs.getString("s.date"));

				psSaleItem = conn.prepareStatement(salesItemQuery);
				psSaleItem.setInt(1, saleId);
				rsSaleItem = psSaleItem.executeQuery();
				
				while (rsSaleItem.next()) {
					String type = rsSaleItem.getString("i.type");
					String servicerCode = rsSaleItem.getString("servicerCode");
					String code = rsSaleItem.getString("itemCode");
					String name = rsSaleItem.getString("i.name");
					String productType = rsSaleItem.getString("si.productType");

					Store store = storeMap.get(storeCode);
					Person customer = personMap.get(customerId);
					Person salesPerson = personMap.get(salesPersonId);
					Sale s = new Sale(saleCode, store, customer, salesPerson, date);
					store.addSale(s);

					if (type.equals("D")) {
						double pricePerGB = rsSaleItem.getDouble("i.basePrice");
						double numGBs = rsSaleItem.getDouble("si.numGBs");
						Data sale = new Data(code, name, pricePerGB, numGBs);
						salesItemMap.put(saleCode, sale);
						
						if(saleMap.containsKey(saleCode)) {
							saleMap.get(saleCode).addSale(sale);
						} else {
							LOGGER.error("Sale not found");
						}
						
					} else if (type.equals("S")) {
						double hourlyPrice = rsSaleItem.getDouble("i.basePrice");
						double numHours = rsSaleItem.getDouble("si.numHours");
						Person servicer = personMap.get(servicerCode);
						Service sale = new Service(code, name, hourlyPrice, numHours, servicer);
						salesItemMap.put(saleCode, sale);
						
						if(saleMap.containsKey(saleCode)) {
							saleMap.get(saleCode).addSale(sale);
						} else {
							LOGGER.error("Sale not found");
						}
						
					} else if (type.equals("V")) {
						double pricePerPeriod = rsSaleItem.getDouble("i.basePrice");
						String phoneNum = rsSaleItem.getString("si.phoneNum");
						int numDays = rsSaleItem.getInt("si.numDays");
						Voice sale = new Voice(code, name, pricePerPeriod, phoneNum, numDays);
						salesItemMap.put(saleCode, sale);
						
						if(saleMap.containsKey(saleCode)) {
							saleMap.get(saleCode).addSale(sale);
						} else {
							LOGGER.error("Sale not found");
						}
						
					} else if (type.equals("P")) {
						double basePrice = rsSaleItem.getDouble("i.basePrice");
						if (productType.equals("P")) {
							Purchase sale = new Purchase(code, name, basePrice);
							salesItemMap.put(saleCode, sale);
							
							if(saleMap.containsKey(saleCode)) {
								saleMap.get(saleCode).addSale(sale);
							} else {
								LOGGER.error("Sale not found");
							}
							
						} else if (productType.equals("L")) {
							LocalDate startDate = LocalDate.parse(rsSaleItem.getString("si.startDate"));
							LocalDate endDate = LocalDate.parse(rsSaleItem.getString("si.endDate"));
							Lease sale = new Lease(code, name, basePrice, startDate, endDate);
							salesItemMap.put(saleCode, sale);
							
							if(saleMap.containsKey(saleCode)) {
								saleMap.get(saleCode).addSale(sale);
							} else {
								LOGGER.error("Sale not found");
							}
						}
					} else {
						LOGGER.error("Invalid type in SaleItem");
					}
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Could not load sale items", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (psSaleItem != null && !psSaleItem.isClosed()) 
				psSaleItem.close();
			if (rsSaleItem != null && !rsSaleItem.isClosed()) 
				rsSaleItem.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}

		return salesItemMap;
	}

}