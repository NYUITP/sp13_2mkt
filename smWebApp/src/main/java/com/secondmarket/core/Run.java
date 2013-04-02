package com.secondmarket.core;
/**
 * This class read in a list of top investors and search through their record to determine
 * the companies that these investors invested in during the past.
 * We will then use this list of company ids and names to further aggregate company specific
 * data.
 * Store all temporary data in HashMaps or Something like that, then at the final stage, store them into one
 * JSONObject.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.Datastore;
import com.secondmarket.common.MongoDBFactory;

public class Run 
{
	protected static Logger logger = Logger.getLogger("core"); 

	public static void main(String args[]) throws IOException, JSONException
	{
		logger.debug("Intializing Data store");
		System.out.println("Initializing Data store");
		Datastore ds = MongoDBFactory.getDataStore();
		
		//Use HashMap to rule out identical companies.
		HashMap<String, String> id_list = new HashMap<String, String>();	
		JSONObject investor = new JSONObject();
		JSONArray invest = new JSONArray();
		
		HashMap<String, String> funding = new HashMap<String, String>();
		HashMap<String, ArrayList<HashMap<Object, Object>>> round = new HashMap<String, ArrayList<HashMap<Object, Object>>>();
		
		System.out.println("Initializing Investor:");
		InitialInvestorObj.initialize(ds, id_list, investor, invest);
		System.out.println("Initializing Company");
		InitialCompanyObj.initialize(ds, funding, round, id_list);
		System.out.println(funding);
		System.out.println(round);
//		logger.debug(funding);
//		logger.debug(round);
	}
}


