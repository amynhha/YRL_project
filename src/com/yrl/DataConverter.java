package com.yrl;

import java.util.Map;

/**
 * Authors: Amy Nguyen and Naomi Post
 * Date: 2024-03-08
 * This program converts the input data into XML and JSON formatted data.
 */
public class DataConverter {
		
		public static void main(String[] args) {
			final String PERSON_FILE = "data/Persons.csv";
			final String ITEM_FILE = "data/Items.csv";
			final String STORE_FILE = "data/Stores.csv";
			
			final String PERSON_XML = "data/Persons.xml";
			final String ITEM_XML = "data/Items.xml";
			final String STORE_XML = "data/Stores.xml";
			
			final String PERSON_JSON = "data/Persons.json";
			final String ITEM_JSON = "data/Items.json";
			final String STORE_JSON = "data/Stores.json";
			
			Map<String, Person> mapPerson = CsvDataLoader.loadPersonData(PERSON_FILE);
			Map<String, Store> mapStore = CsvDataLoader.loadStoreData(STORE_FILE, mapPerson);
			Map<String, Item> mapItem = CsvDataLoader.loadItemData(ITEM_FILE);
			
			ConvertData.convertPersonsXML(mapPerson, PERSON_XML);
			ConvertData.convertPersonsJSON(mapPerson, PERSON_JSON);
			
			ConvertData.convertItemsXML(mapItem, ITEM_XML);
			ConvertData.convertItemsJSON(mapItem, ITEM_JSON);
			
			ConvertData.convertStoresXML(mapStore, STORE_XML);
			ConvertData.convertStoresJSON(mapStore, STORE_JSON);
		}

}
