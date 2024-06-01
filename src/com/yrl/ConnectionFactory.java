package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Authors: Amy Nguyen and Naomi Post 
 * Date: 2024-04-11
 * This class opens a connection to the database.
 */
public class ConnectionFactory {

	private static final Logger LOGGER = LogManager.getLogger(ConnectionFactory.class);
	
	public static Connection getConnection() {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			LOGGER.error("Unable to make connection",e);
			e.printStackTrace();
		}
		return conn;
	}
	
}
