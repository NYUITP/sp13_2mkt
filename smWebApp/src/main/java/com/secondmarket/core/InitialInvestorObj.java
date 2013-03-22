package com.secondmarket.core;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import org.json.*;
//import org.apache.commons.io.IOUtils;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class InitialInvestorObj {
	public void initialize(Datastore ds,HashMap companyList,HashMap id_list,JSONObject Investor,
			JSONArray invest) throws IOException, JSONException{
		AngelCrunch second = new AngelCrunch();
		
		/*
		 * Read in the InvestorList.txt file
		 */
		FileReader file = new FileReader("InvestorList1.txt");
		BufferedReader buff = new BufferedReader(file);
		boolean eof = false;
		
		/*
		 * Go through the investorList file, put company ids and names into
		 * the HashMap.
		 */
		while(!eof){
			/*
			 * Read each line of record, use slug to search for investor
			 */
				String slug = buff.readLine();
				if (slug == null)
					eof = true;
				else {
					String investor_info = second.searchAngelInvestor(slug);
//					System.out.println(investor_info);
					/*
					 * get the investor id
					 */
					String investor_id = second.getfield(investor_info,"id");
					String investor_name = second.getfield(investor_info,"name");
					String investor_bio = second.getfield(investor_info,"bio");
					String investor_follower_count = second.getfield(investor_info,"follower_count");
//					String investor_ = second.getfield(investor_info,"bio");
					
//					System.out.println("Investor id: "+investor_id + " Investor name: "+investor_name);
					/*
					 * get start-up role
					 */
					
					String start_up_role = second.getStartUpRole(investor_id);
//					System.out.println("start_up_role:\n");
//					System.out.println(start_up_role);
					/*
					 * Now call hashCompanyList(String start_up_role,HashMap
					 * companyList)
					 */
//					System.out.println(start_up_role);
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
					
					int company_count = 0;
					for (Object key:this_id_list.keySet()){
						JSONObject jobj = new JSONObject();
						jobj.put("company_id", key.toString());
						jobj.put("company_name", this_id_list.get(key).toString());
						startup_invested.put(jobj);
						company_count++;
					}
					
					/**
					 * Put put put! 
					 */
					each_investor.put("company_count", company_count);
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
					
					People user = new People(each_investor);
					ds.save(user);
				}
		}
	}
}
