package com.secondmarket.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.Datastore;
import com.secondmarket.domain.CompanyEnum;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.InvestorEnum;

public class InitialInvestorObj 
{
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static boolean initialize(Datastore ds, 
			HashMap<String, String> id_list, JSONObject Investors,
			JSONArray invest) throws IOException, JSONException
	{
		// Read in the InvestorList.txt file
		FileReader file = new FileReader("InvestorList.txt");
		BufferedReader buff = new BufferedReader(file);
		boolean eof = false;
		
		//Go through the investorList file, put company ids and names into the HashMap.
		while(!eof)
		{
			 	//Read each line of record, use slug to search for investor
				String slug = buff.readLine();
				if (slug == null)
				{
					eof = true;
				}
				else 
				{
					String investor_info = AngelCrunch.searchAngelInvestor(slug);
					String investor_id = AngelCrunch.getfield(investor_info,InvestorEnum.ID.getLabel().toString());
					String investor_name = AngelCrunch.getfield(investor_info,InvestorEnum.NAME.getLabel().toString());
					String investor_bio = AngelCrunch.getfield(investor_info,InvestorEnum.BIO.getLabel().toString());
					String investor_follower_count = AngelCrunch.getfield(investor_info,InvestorEnum.FOLLOWER_COUNT.getLabel().toString());
			
					String start_up_role = AngelCrunch.getStartUpRole(investor_id);//get start-up role		
					
					//Now call hashCompanyList(String start_up_role,HashMap companyList)
					AngelCrunch.hashCompanyList(start_up_role, id_list);
					
					//Just for this specific investor
					HashMap<String, String> this_id_list = new HashMap<String, String>();
					AngelCrunch.hashCompanyList(start_up_role, this_id_list);
					
					//Push company_id and company_name into JSONArray, first initiate JSONArray
					JSONObject each_investor = new JSONObject(); 
					JSONArray startup_invested = new JSONArray();

					//Iterate through id_list related to each investor
					int company_count = 0;
					for (Object key:this_id_list.keySet()){
						JSONObject jobj = new JSONObject();
						jobj.put(CompanyEnum.ID.getLabel().toString(), key.toString());
						jobj.put(CompanyEnum.NAME.getLabel().toString(), this_id_list.get(key).toString());
						startup_invested.put(jobj);
						company_count++;
					}
					//Put put put! 
					each_investor.put(InvestorEnum.COMPANY_COUNT.getLabel().toString(), company_count);
					each_investor.put(InvestorEnum.STARTUP_INVESTED.getLabel().toString(),startup_invested);
					each_investor.put(InvestorEnum.ID.getLabel().toString(), investor_id);
					each_investor.put(InvestorEnum.NAME.getLabel().toString(),investor_name);
					each_investor.put(InvestorEnum.BIO.getLabel().toString(), investor_bio);
					each_investor.put(InvestorEnum.FOLLOWER_COUNT.getLabel().toString(), investor_follower_count);
					logger.debug(each_investor);
					
					/**
					 * Noted that for this stage of the project, we need only the
					 * each_investor JSONObject. Zoe you can start here to push 
					 * these investors into the MongoDB
					 */
					//This is for future reference. A giant JSONObject that contains all the investors.
					invest.put(each_investor);
					Investors.put(InvestorEnum.INVESTOR_INFO.getLabel().toString(), invest);
					Investor user = new Investor(each_investor);
					ds.save(user);
				}
		}
		buff.close();
		return true;
	}
}
