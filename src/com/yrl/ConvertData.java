package com.yrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/**
 * Authors: Amy Nguyen and Naomi Post
 * Date: 2024-03-08
 * This class contains methods to convert the data to XML and JSON according to type.
 */
public class ConvertData {

	/**
	 *Convert Persons from CSV to XML, then write in an output file.
	 *
	 *@param personMap, outputFile
	 */
	public static void convertPersonsXML(Map<String, Person> personMap, String outputFile) {
		if(personMap == null || personMap.size() == 0) {
			return;
		}
		XStream xstream = new XStream();
		File personXML = new File(outputFile);
		PrintWriter pwxml;
		try {
			pwxml = new PrintWriter(personXML);
			pwxml.println("<persons>");
			for(Person p : personMap.values()) {
				String xml = xstream.toXML(p);
				pwxml.println(xml);
			}
			pwxml.println("</persons>");
			pwxml.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 *Convert Persons from CSV to JSON, then write in an output file.
	 *
	 *@param personMap, outputFile
	 */
	public static void convertPersonsJSON(Map<String, Person> personMap, String outputFile) {
		if(personMap == null || personMap.size() == 0) {
			return;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File personJSON = new File(outputFile);
		PrintWriter pwjson;
		try {
			pwjson = new PrintWriter(personJSON);
			pwjson.println("Persons.json:");
			pwjson.println("{");
			pwjson.println("\"persons\": [");
			for(Person p : personMap.values()) {
				String json = gson.toJson(p);
				pwjson.println(json + ',');
			}
			pwjson.println("]}");
			pwjson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 *Convert Items from CSV to XML, then write in an output file.
	 *
	 *@param itemMap, outputFile
	 */
	public static void convertItemsXML(Map<String, Item> itemMap, String outputFile) {
		if(itemMap == null || itemMap.size() == 0) {
			return;
		}
		XStream xstream = new XStream();
		File itemXML = new File(outputFile);
		PrintWriter pwxml;
		try {
			pwxml = new PrintWriter(itemXML);
			pwxml.println("<items>");
			for(Item i : itemMap.values()) {
				String xml = xstream.toXML(i);
				pwxml.println(xml);
			}
			pwxml.println("</items>");
			pwxml.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *Convert Items from CSV to JSON, then write in an output file.
	 *
	 *@param itemMap, outputFile
	 */
	public static void convertItemsJSON(Map<String, Item> itemMap, String outputFile) {
		if(itemMap == null || itemMap.size() == 0) {
			return;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File itemJSON = new File(outputFile);
		PrintWriter pwjson;
		try {
			pwjson = new PrintWriter(itemJSON);
			pwjson.println("Items.json:");
			pwjson.println("{");
			pwjson.println("\"items\": [");
			for(Item i : itemMap.values()) {
				String json = gson.toJson(i);
				pwjson.println(json + ',');
			}
			pwjson.println("]}");
			pwjson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *Convert Stores from CSV to XML, then write in an output file.
	 *
	 *@param storeMap, outputFile
	 */
	public static void convertStoresXML(Map<String, Store> storeMap, String outputFile) {
		if(storeMap == null || storeMap.size() == 0) {
			return;
		}
		XStream xstream = new XStream();
		File storeXML = new File(outputFile);
		PrintWriter pwxml;
		try {
			pwxml = new PrintWriter(storeXML);
			pwxml.println("<stores>");
			for(Store s : storeMap.values()) {
				String xml = xstream.toXML(s);
				pwxml.println(xml);
			}
			pwxml.println("</stores>");
			pwxml.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *Convert Stores from CSV to JSON, then write in an output file.
	 *
	 *@param storeMap, outputFile
	 */
	public static void convertStoresJSON(Map<String, Store> storeMap, String outputFile) {
		if(storeMap == null || storeMap.size() == 0) {
			return;
		}	
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File storeJSON = new File(outputFile);
		PrintWriter pwjson;
		try {
			pwjson = new PrintWriter(storeJSON);
			pwjson.println("Stores.json:");
			pwjson.println("{");
			pwjson.println("\"stores\": [");
			for(Store s : storeMap.values()) {
				String json = gson.toJson(s);
				pwjson.println(json + ',');
			}
			pwjson.println("]}");
			pwjson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
