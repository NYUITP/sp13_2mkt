package com.secondmarket.core;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import org.json.*;
//import org.apache.commons.io.IOUtils;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class InitialCompanyObj {
	public void initialize(HashMap funding, HashMap round, HashMap slug_id, HashMap id_list,
			Datastore ds) throws JSONException{
		AngelCrunch second = new AngelCrunch();
		for (Object key1:id_list.keySet().toArray()){
			/**
			 * New JSONObject each_company. This is the JSONObject
			 * that will be put into the MongoDB as Companies
			 */
			JSONObject each_company = new JSONObject();
			
			String key = key1.toString();
			String name = (id_list.get(key1)).toString();
//			System.out.println(key + " and company name: " + name);
			String getFromAngel = second.getangelHTML(key);
//			System.out.println(getFromAngel);
			JSONObject jobj = second.parseToJSON(getFromAngel);
			String slug = second.getCrunchSlug(jobj);
			/**
			* Additional fields
			*/
			int follower_count = second.getFollowerCount(jobj);
			int quality = second.getQuality(jobj);
			String angellist_url = second.getAngelListUrl(jobj);
//			System.out.println(name);
			if (!slug.isEmpty()) {
				String crunchCompany = null; 
				crunchCompany = second.getcrunchHTML(slug);
				String total_funding = null;
				total_funding = second.getCrunchTotalFund(crunchCompany);
//				System.out.println(total_funding);
//				System.out.println(name);
				if (!total_funding.equals("$0")){
					each_company.put("follower_count",follower_count);
					each_company.put("quality",quality);
					each_company.put("angellist_url",angellist_url);
					each_company.put("company_id", key);
					each_company.put("company_name", name);

					// System.out.println(name);
					// System.out.println("crunchCompany:\n");
					// System.out.println(crunchCompany);
					// Later: Process the total_funding amount using regular
					// expression and parse it to double.
					each_company.put("total_funding", total_funding);
					// HashMap round_funding =
					// second.getCrunchRoundFunding(crunchCompany);
//					System.out.println(total_funding);
					// System.out.println(round_funding);
					each_company.put("funding_rounds",
							second.getCrunchRoundFunding(crunchCompany));
					second.crunchCompanyInfo(crunchCompany, key, funding, round);

					// if(!each_company.get("total_funding").equals("$0"))
					System.out.println(each_company);
					/**
					 * Here each_company is the JSONObject returned for each of
					 * the company. Zoe, you must push the JSONObject to MongoDB
					 * from HERE!!!!! This is really important! I have discarded
					 * all companies with $0 total funding amount and null fields!
					 */
					
					Company comp = new Company(each_company);
					ds.save(comp);
			
				}
				

			} else
				continue;

		}
	}
}
