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

import com.mongodb.DBCollection;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.MongoDBFactory;

public class Run 
{
	protected static Logger logger = Logger.getLogger("core"); 

	public static void main(String args[]) throws IOException, JSONException
	{
		logger.debug("Start getting collections from MongoDB");
		DBCollection companyColl = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve collection
		DBCollection investorColl = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString());// Retrieve collection
		
		//Use HashMap to rule out identical companies.
		HashMap<String, String> id_list = new HashMap<String, String>();	
		JSONObject investor = new JSONObject();
		JSONArray invest = new JSONArray();
		
		HashMap<String, String> funding = new HashMap<String, String>();
		HashMap<String, ArrayList<HashMap<Object, Object>>> round = new HashMap<String, ArrayList<HashMap<Object, Object>>>();
		
		InitialInvestorObj.initialize(investorColl, id_list, investor, invest);
		InitialCompanyObj.initialize(companyColl, funding, round, id_list);
		logger.debug(funding);
		logger.debug(round);
	}
}


