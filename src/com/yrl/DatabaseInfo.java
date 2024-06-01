package com.yrl;

/**
 * Database connection configuration 
 */
public class DatabaseInfo {

	public static final String USERNAME = "anguyen105";
	public static final String PASSWORD = "Nathahlae1ah";
	public static final String PARAMETERS = "";
	public static final String SERVER = "cse-linux-01.unl.edu";
	
	public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);
	
}
