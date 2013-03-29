package com.secondmarket.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.secondmarket.domain.CompanyEnum;

public class InitialCompanyObj 
{
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static boolean initialize(DBCollection coll,
			HashMap<String, String> funding, 
			HashMap<String, ArrayList<HashMap<Object, Object>>>round, 
			HashMap<String, String> id_list) throws JSONException
	{
		for (Object key1 : id_list.keySet().toArray())
		{
			// New JSONObject each_company. This is the JSONObject that will be put into the MongoDB as Companies
			JSONObject each_company = new JSONObject();
			String key = key1.toString();
			String name = (id_list.get(key1)).toString();
			String getFromAngel = AngelCrunch.getangelHTML(key);
			JSONObject jobj = AngelCrunch.parseToJSON(getFromAngel);
			String slug = AngelCrunch.getCrunchSlug(jobj);
			
			//Additional fields
			int follower_count = AngelCrunch.getFollowerCount(jobj);
			int quality = AngelCrunch.getQuality(jobj);
			String angellist_url = AngelCrunch.getAngelListUrl(jobj);
			
			if (!slug.isEmpty())
			{
				String crunchCompany = null; 
				crunchCompany = AngelCrunch.getcrunchHTML(slug);
				String total_funding = null;
				total_funding = AngelCrunch.getCrunchTotalFund(crunchCompany);
				
				if (!total_funding.equals("$0"))
				{
					each_company.put(CompanyEnum.FOLLOWER_COUNT.getLabel().toString(),follower_count);
					each_company.put(CompanyEnum.QUALITY.getLabel().toString(),quality);
					each_company.put(CompanyEnum.ANGLELIST_URL.getLabel().toString(),angellist_url);
					each_company.put(CompanyEnum.ID.getLabel().toString(), key);
					each_company.put(CompanyEnum.NAME.getLabel().toString(), name);
					
					// Later: Process the total_funding amount using regular expression and parse it to double.
					each_company.put(CompanyEnum.TOTAL_FUNDING.getLabel().toString(), total_funding);
					each_company.put(CompanyEnum.FUNDING_ROUNDS.getLabel().toString(), AngelCrunch.getCrunchRoundFunding(crunchCompany));
					AngelCrunch.crunchCompanyInfo(crunchCompany, key, funding, round);

					// if(!each_company.get("total_funding").equals("$0"))
					System.out.println(each_company);
					/**
					 * Here each_company is the JSONObject returned for each of
					 * the company. Zoe, you must push the JSONObject to MongoDB
					 * from HERE!!!!! This is really important! I have discarded
					 * all companies with $0 total funding amount and null fields!
					 */
					DBObject dbObject = (DBObject)JSON.parse(each_company.toString());
					coll.insert(dbObject);
				}
			} else
				continue;
		}
		return true;
	}
}
