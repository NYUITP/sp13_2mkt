package com.secondmarket.core;
/**
 * This class read in a list of top investors and search through their record to determine
 * the companies that these investors invested in during the past.
 * We will then use this list of company ids and names to further aggregate company specific
 * data.
 * Store all temporary data in HashMaps or Something like that, then at the final stage, store them into one
 * JSONObject.
 */

import java.io.* ;
import java.net.* ;
import java.util.* ;
import org.json.*;
//import org.apache.commons.io.IOUtils;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class Run {
	public static void main(String args[]) throws IOException, JSONException{
		/**
		 * Initialize Datastore ds
		 */
		System.out.println("Start initialization of MongoDB");
		InitialDB init = new InitialDB();
		Datastore ds = init.initialize();
		
//		System.out.println("Start process!");
//		Mongo mongo = new Mongo("localhost", 27017);
//		System.out.println("Check 0");
//		Morphia morphia = new Morphia();
//		System.out.println("Check 1");
//		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
//		System.out.println("success!");
		/*
		 * Use HashMap to rule out identical companies.
		 */
		HashMap companyList = new HashMap();
//		HashMap companySlugList = new HashMap();
		HashMap id_list = new HashMap();
//		JSONObject Investor = new JSONObject();		
		JSONObject Investor = new JSONObject();
		JSONArray invest = new JSONArray();
		
		HashMap funding = new HashMap();
		HashMap round = new HashMap();
		HashMap slug_id = new HashMap();
		
		InitialInvestorObj investObj = new InitialInvestorObj();
		investObj.initialize(ds, companyList, id_list, Investor, invest);
		InitialCompanyObj companyObj = new InitialCompanyObj();
		companyObj.initialize(funding, round, slug_id, id_list, ds);
		


		System.out.println(funding);
		System.out.println(round);
	}
}


