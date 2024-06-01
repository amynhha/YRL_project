package com.yrl;

/**
 * Database connection configuration 
 */
public class DatabaseInfo {

	public static final String USERNAME = "IMPLEMENT";
	public static final String PASSWORD = "IMPLEMENT";
	public static final String PARAMETERS = "";
	public static final String SERVER = "IMPLEMENT";
	
	public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);
	
}
