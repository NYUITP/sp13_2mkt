package com.secondmarket.core;
/**
 * This class read in a list of top investors and search through their record to determine
 * the companies that these investors invested in during the past.
 * We will then use this list of company ids and names to further aggregate company specific
 * data.
 * Store all temporary data in HashMaps or Something like that, then at the final stage, store them into one
 * JSONObject.
 * @author Shenglun Shi
 */

import java.io.* ;
import java.net.* ;
import java.util.* ;
import org.json.*;
//import org.apache.commons.io.IOUtils;

public class Run {
	public static void main(String args[]) throws IOException, JSONException{
		AngelCrunch second = new AngelCrunch();
		/*
		 * Use HashMap to rule out identical companies.
		 */
		
		HashMap companyList = new HashMap();
//		HashMap companySlugList = new HashMap();
		HashMap id_list = new HashMap();
//		JSONObject Investor = new JSONObject();
		    
		/*
		 * Read in the InvestorList.txt file
		 */
		FileReader file = new FileReader("InvestorList11.txt");
		BufferedReader buff = new BufferedReader(file);
		boolean eof = false;
		
		/*
		 * Go through the investorList file, put company ids and names into
		 * the HashMap.
		 */
		JSONObject Investor = new JSONObject();
		JSONArray invest = new JSONArray();
		while(!eof){
		/*
		 * Read each line of record, use slug to search for investor
		 */
			String slug = buff.readLine();
			if (slug == null)
				eof = true;
			else {
				String investor_info = second.searchAngelInvestor(slug);
//				System.out.println(investor_info);
				/*
				 * get the investor id
				 */
				String investor_id = second.getfield(investor_info,"id");
				String investor_name = second.getfield(investor_info,"name");
				String investor_bio = second.getfield(investor_info,"bio");
				String investor_follower_count = second.getfield(investor_info,"follower_count");
//				String investor_ = second.getfield(investor_info,"bio");
				
//				System.out.println("Investor id: "+investor_id + " Investor name: "+investor_name);
				/*
				 * get start-up role
				 */
				
				String start_up_role = second.getStartUpRole(investor_id);
//				System.out.println("start_up_role:\n");
//				System.out.println(start_up_role);
				/*
				 * Now call hashCompanyList(String start_up_role,HashMap
				 * companyList)
				 */
//				System.out.println(start_up_role);
				second.hashCompanyList(start_up_role, id_list);
				//Just for this specific investor
				HashMap this_id_list = new HashMap();
				second.hashCompanyList(start_up_role, this_id_list);
				/**
				 * Push company_id and company_name into JSONArray, first initiate JSONArray
				 */
				JSONObject each_investor = new JSONObject();
				JSONArray startup_invested = new JSONArray();
				
				/**
				 * Iterate through id_list related to each investor
				 */
				for (Object key:this_id_list.keySet()){
					JSONObject jobj = new JSONObject();
					jobj.put("company_id", key.toString());
					jobj.put("company_name", this_id_list.get(key).toString());
					startup_invested.put(jobj);
				}
				
				/**
				 * Put put put! 
				 */
				each_investor.put("startup_invested",startup_invested);
				each_investor.put("investor_id", investor_id);
				each_investor.put("investor_name",investor_name);
				each_investor.put("investor_bio", investor_bio);
				each_investor.put("follower_count", investor_follower_count);
				System.out.println(each_investor);
				
				/**
				 * Noted that for this stage of the project, we need only the
				 * each_investor JSONObject. Zoe you can start here to push 
				 * these investors into the MongoDB
				 */
			
				
				
				/*
				 * This is for future reference. A giant JSONObject that contains
				 * all the investors.
				 */
				invest.put(each_investor);
				Investor.put("Investor_information", invest);
			}
//			System.out.println(Investor);
		}
	
//		System.out.println(companyList);
//		System.out.println(companyList.size());
		
		/*
		 * Now start to pull company data
		 */
		
		/*
		 * Get the ids out of the HashMap, use ids to locate company profile
		 * Comment out when testing.
		 */
		/*
		 * Check return of crunchBase, found "total_money_raised":"$48.5M"
		 * and "funding_rounds":[], within each round we have the "round_code","source_description","raised_amount",
		 * "funded_year","funded_month", and so many other fields that we could use.
		 * Let's play!
		 */
		HashMap funding = new HashMap();
		HashMap round = new HashMap();
		HashMap slug_id = new HashMap();
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
					/**
					 * Start from here Zoe!
					 */
					
					
					
					
					/**
					 * End here
					 */
					
				}
				

			} else
				continue;

		}

		System.out.println(funding);
		System.out.println(round);
	}
}


