package com.yrl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class SalesData {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.WARN);
		LOGGER.info("Started...");
	}

	/**
	 * A helper method to remove all data from a specific table.
	 * 
	 * @param conn
	 * @param tableName
	 */
	private static void clearTable(Connection conn, String tableName) {

		PreparedStatement ps = null;

		String query = "DELETE FROM " + tableName;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not delete the table " + tableName);
			throw new RuntimeException(e);
		}

		try {
			if (ps != null && !ps.isClosed())
				ps.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {

		Connection conn = null;
		LOGGER.debug("DEBUG: Attempting to clear database");

		conn = ConnectionFactory.getConnection();

		clearTable(conn, "SaleItem");
		clearTable(conn, "Sale");
		clearTable(conn, "Item");
		clearTable(conn, "Store");
		clearTable(conn, "Email");
		clearTable(conn, "Person");
		clearTable(conn, "Address");

		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method to add an Address record to the database with the provided data. 
	 * Returns the generated addressId key.
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @return addressId
	 */
	public static int addAddress(String street, String city, String state, String zip) {

		Connection conn = null;
		conn = ConnectionFactory.getConnection();

		String query = "INSERT INTO Address (street,city,state,zip) VALUES (?,?,?,?)";
		PreparedStatement ps = null;

		int addressId;

		try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			addressId = keys.getInt(1);
			keys.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Could not add address");
			throw new RuntimeException(e);
		}
		
		try {
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}
		
		return addressId;
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addPerson(String personUuid, String firstName, String lastName, String street, String city,
			String state, String zip) {

		Connection conn = null;

		conn = ConnectionFactory.getConnection();

		String query = "SELECT addressId FROM Address a WHERE a.street = ? AND a.city = ? AND a.state = ? AND a.zip = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int addressId;

		try {
			/* Get addressId */
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			rs = ps.executeQuery();
			if (rs.next()) {
				addressId = rs.getInt("addressId");
			} else {
				addressId = addAddress(street,city,state,zip);
			}
			rs.close();

			/* Insert person */
			query = "INSERT INTO Person (uuid,lastName,firstName,addressId) VALUES (?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setString(1, personUuid);
			ps.setString(2, lastName);
			ps.setString(3, firstName);
			ps.setInt(4, addressId);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add person");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(String personUuid, String email) {
		
		Connection conn = null;

		conn = ConnectionFactory.getConnection();

		String query = "SELECT personId FROM Person p WHERE p.uuid = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int personId;

		try {
			/* Get personId */
			ps = conn.prepareStatement(query);
			ps.setString(1, personUuid);
			rs = ps.executeQuery();
			if (rs.next()) {
				personId = rs.getInt("personId");
			} else {
				LOGGER.error("Could not locate person with uuid = " + personUuid);
				return;
			}
			rs.close();

			/* Insert email */
			query = "INSERT INTO Email (email,personId) VALUES (?,?)";

			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setInt(2, personId);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add email");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip) {
		Connection conn = null;

		conn = ConnectionFactory.getConnection();

		String query = "SELECT addressId FROM Address a WHERE a.street = ? AND a.city = ? AND a.state = ? AND a.zip = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int addressId;
		int personId;

		try {
			/* Get addressId */
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			rs = ps.executeQuery();
			if (rs.next()) {
				addressId = rs.getInt("addressId");
			} else {
				addressId = addAddress(street,city,state,zip);
			}
			
			/* Get personId corresponding to the manager */
			query = "SELECT personId FROM Person p WHERE p.uuid = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, managerCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				personId = rs.getInt("personId");
			} else {
				LOGGER.error("Could not locate person with uuid = " + managerCode);
				return;
			}
			rs.close();

			/* Insert store */
			query = "INSERT INTO Store (storeCode,personId,addressId) VALUES (?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			ps.setInt(2, personId);
			ps.setInt(3, addressId);
			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Could not add store");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an item record to the database of the given <code>type</code> with the
	 * given <code>code</code>, <code>name</code> and <code>basePrice</code>.
	 *
	 * Valid values for the <code>type</code> will be <code>"Product"</code>,
	 * <code>"Service"</code>, <code>"Data"</code>, or <code>"Voice"</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param type
	 * @param basePrice
	 */
	public static void addItem(String code, String name, String type, double basePrice) {
		Connection conn = null;

		conn = ConnectionFactory.getConnection();
		
		String query = "INSERT INTO Item (code,name,type,basePrice) VALUES (?,?,?,?)";
		
		if(type.equals("Data")) {
			type = "D";
		} else if (type.equals("Voice")) {
			type = "V";
		} else if (type.equals("Product")) {
			type = "P";
		} else if (type.equals("Service")) {
			type = "S";
		}

		PreparedStatement ps = null;

		try {
			/* Add item to Item table */
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, type);
			ps.setDouble(4, basePrice);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add Item");
			throw new RuntimeException(e);
		}

		try {
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an Sale record to the database with the given data.
	 *
	 * @param saleCode
	 * @param storeCode
	 * @param customerPersonUuid
	 * @param salesPersonUuid
	 * @param saleDate
	 */
	public static void addSale(String saleCode, String storeCode, String customerPersonUuid, String salesPersonUuid,
			String saleDate) {
		
		Connection conn = null;

		conn = ConnectionFactory.getConnection();
		
		String query = "SELECT personId FROM Person p WHERE p.uuid = ?";
		int customerId;
		int salesPersonId;
		int storeId;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			/* Get customer UUID */
			ps = conn.prepareStatement(query);
			ps.setString(1, customerPersonUuid);
			rs = ps.executeQuery();
			if (rs.next()) {
				customerId = rs.getInt("personId");
			} else {
				LOGGER.error("Could not locate person with uuid = " + customerPersonUuid);
				return;
			}
			
			/* Get manager UUID */
			ps = conn.prepareStatement(query);
			ps.setString(1, salesPersonUuid);
			rs = ps.executeQuery();
			if (rs.next()) {
				salesPersonId = rs.getInt("personId");
			} else {
				LOGGER.error("Could not locate person with uuid = " + salesPersonUuid);
				return;
			}
			rs.close();
			
			/* Get storeCode */
			query = "SELECT storeId FROM Store st WHERE st.storeCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				storeId = rs.getInt("storeId");
			} else {
				LOGGER.error("Could not locate sale with storeCode = " + storeCode);
				return;
			}
			rs.close();
			
			/* Add sale to Sale table */
			query = "INSERT INTO Sale (saleCode,storeId,customerId,salesPersonId,date) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			ps.setInt(2, storeId);
			ps.setInt(3, customerId);
			ps.setInt(4, salesPersonId);
			ps.setString(5, saleDate);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add sale");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>).
	 *
	 * @param saleCode
	 * @param itemCode
	 */
	public static void addProductToSale(String saleCode, String itemCode) {
		Connection conn = null;

		conn = ConnectionFactory.getConnection();
		
		String query = "SELECT itemId FROM Item i WHERE i.code = ?";
		int itemId;
		int saleId;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			/* Get itemCode*/
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			} else {
				LOGGER.error("Could not locate item with itemCode = " + itemCode);
				return;
			}
			
			/* Get saleCode */
			query = "SELECT saleId FROM Sale s WHERE s.saleCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				saleId = rs.getInt("saleId");
			} else {
				LOGGER.error("Could not locate sale with saleCode = " + saleCode);
				return;
			}
			rs.close();
			
			/* Add Product to SaleItem table */
			query = "INSERT INTO SaleItem (saleId,productType,itemId) VALUES (?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setInt(1, saleId);
			ps.setString(2, "P");
			ps.setInt(3, itemId);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add Product");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular leased (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the start/end date
	 * specified.
	 *
	 * @param saleCode
	 * @param startDate
	 * @param endDate
	 */
	public static void addLeaseToSale(String saleCode, String itemCode, String startDate, String endDate) {
		
		Connection conn = null;

		conn = ConnectionFactory.getConnection();

		String query = "SELECT itemId FROM Item i WHERE i.code = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId;
		int saleId;

		try {
			/* Get itemId */
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			} else {
				LOGGER.error("Could not locate item with code = " + itemCode);
				return;
			}
			
			/* Get saleId */
			query = "SELECT saleId FROM Sale s WHERE s.saleCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				saleId = rs.getInt("saleId");
			} else {
				LOGGER.error("Could not locate sale with code = " + saleCode);
				return;
			}
			rs.close();

			/* Add to sale (aka insert into SaleItem) */
			query = "INSERT INTO SaleItem (itemId,saleId,productType,startDate,endDate) VALUES (?,?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setInt(1, itemId);
			ps.setInt(2, saleId);
			ps.setString(3,"L");
			ps.setString(4, startDate);
			ps.setString(5, endDate);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add lease to sale");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of hours. The service is done by the employee with the specified
	 * <code>servicePersonUuid</code>
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param billedHours
	 * @param servicePersonUuid
	 */
	public static void addServiceToSale(String saleCode, String itemCode, double billedHours,
			String servicePersonUuid) {
		
		Connection conn = null;

		conn = ConnectionFactory.getConnection();

		String query = "SELECT itemId FROM Item i WHERE i.code = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId;
		int saleId;
		int servicerId;

		try {
			/* Get itemId */
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			} else {
				LOGGER.error("Could not locate item with code = " + itemCode);
				return;
			}
			
			/* Get saleId */
			query = "SELECT saleId FROM Sale s WHERE s.saleCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				saleId = rs.getInt("saleId");
			} else {
				LOGGER.error("Could not locate sale with code = " + saleCode);
				return;
			}
			
			/* Get personId corresponding to servicePersonUuid */
			query = "SELECT personId FROM Person p WHERE p.uuid = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, servicePersonUuid);
			rs = ps.executeQuery();
			if (rs.next()) {
				servicerId = rs.getInt("personId");
			} else {
				LOGGER.error("Could not locate person with uuid = " + servicePersonUuid);
				return;
			}
			rs.close();

			/* Add to sale (aka insert into SaleItem) */
			query = "INSERT INTO SaleItem (itemId,saleId,numHours,servicerId) VALUES (?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setInt(1, itemId);
			ps.setInt(2, saleId);
			ps.setDouble(3, billedHours);
			ps.setInt(4, servicerId);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add service to sale");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular data plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of gigabytes.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param gbs
	 */
	public static void addDataPlanToSale(String saleCode, String itemCode, double gbs) {
		Connection conn = null;

		conn = ConnectionFactory.getConnection();
		
		String query = "SELECT itemId FROM Item i WHERE i.code = ?";
		int itemId;
		int saleId;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			/* Get itemCode*/
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			} else {
				LOGGER.error("Could not locate item with itemCode = " + itemCode);
				return;
			}
			
			/* Get saleCode */
			query = "SELECT saleId FROM Sale s WHERE s.saleCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				saleId = rs.getInt("saleId");
			} else {
				LOGGER.error("Could not locate sale with saleCode = " + saleCode);
				return;
			}
			rs.close();
			
			/* Add DataPlan to SaleItem table */
			query = "INSERT INTO SaleItem (saleId,itemId,numGBs) VALUES (?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setInt(1, saleId);
			ps.setInt(2, itemId);
			ps.setDouble(3,gbs);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add Data plan");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular voice plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * <code>phoneNumber</code> for the given number of <code>days</code>.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param phoneNumber
	 * @param days
	 */
	public static void addVoicePlanToSale(String saleCode, String itemCode, String phoneNumber, int days) {
		
		Connection conn = null;

		conn = ConnectionFactory.getConnection();

		String query = "SELECT itemId FROM Item i WHERE i.code = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId;
		int saleId;

		try {
			/* Get itemId */
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			} else {
				LOGGER.error("Could not locate item with code = " + itemCode);
				return;
			}
			
			/* Get saleId */
			query = "SELECT saleId FROM Sale s WHERE s.saleCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				saleId = rs.getInt("saleId");
			} else {
				LOGGER.error("Could not locate sale with code = " + saleCode);
				return;
			}
			
			rs.close();

			/* Add to sale (aka insert into SaleItem) */
			query = "INSERT INTO SaleItem (itemId,saleId,phoneNum,numDays) VALUES (?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setInt(1, itemId);
			ps.setInt(2, saleId);
			ps.setString(3, phoneNumber);
			ps.setInt(4, days);
			ps.executeUpdate();
			
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Could not add service to sale");
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
			LOGGER.error("Could not close resources");
			throw new RuntimeException(e);
		}

	}

}
