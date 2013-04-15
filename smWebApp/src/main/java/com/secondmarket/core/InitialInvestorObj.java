package com.secondmarket.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.Datastore;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.domain.Investor;

public class InitialInvestorObj 
{
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static boolean initialize(Datastore ds, 
			HashMap<String, String> id_list, JSONObject investors,
			JSONArray invest, HashMap<String,Integer> counter,
			HashMap<String,String> personPermalink) throws IOException, JSONException
	{
		// Read in the InvestorList.txt file
		FileReader file = new FileReader("InvestorList11.txt");
		BufferedReader buff = new BufferedReader(file);
		boolean eof = false;
		// Counter for calling AngelList API
		int cc = 0;
		//Go through the investorList file, put company ids and names into the HashMap.
		while(!eof)
		{
			if(counter.get("count") > 950){
				try {
			        Thread.sleep(1000 * 60 * 60);
			        counter.put("count", 0);
			        cc = 0;
			    } catch (InterruptedException ex) {}
			} else{
				//Read each line of record, use slug to search for investor
				String slug = buff.readLine();
				if (slug == null)
				{
					eof = true;
				}
				else 
				{
					//First time call AngelList API++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
					String investor_info = AngelCrunch.searchAngelInvestor(slug);
					cc += 1;
					counter.put("count", cc);
					if(investor_info !=null && !investor_info.equals("") &&!investor_info.isEmpty())
					{
						String type = "people";
						Investor user = createInvestorObject(id_list, investors, invest,
								personPermalink, investor_info, type);
						ds.save(user);
					}
				}
			}
		}
		
		/*
		 * Start to populate company investors
		 */
		HashMap<String,String> fin_org_list = AngelCrunch.getCrunchEntityList("financial-organizations");
		for(Object key:fin_org_list.keySet()){
			String permalink = key.toString();
			if(personPermalink.containsKey(permalink)){
				continue;
			}else{
				// First check if this person is in AngelList, first seperate
				// name
				// https://api.angel.co/1/search?query=reid+hoffman&type=User
				String name = fin_org_list.get(key);
				String[] spt = name.split(" ");
				String search_name = "";
				for (int i = 0; i < spt.length - 1; i++) {
					search_name += spt[i];
					search_name += "+";
				}
				search_name += spt[spt.length - 1];
				String investor_info = AngelCrunch.searchAngelInvestor3(search_name);
				if(investor_info !=null && !investor_info.equals("") &&!investor_info.isEmpty())
				{
					String type = "financial-organizations";
					Investor user = createFin_org_InvestorObject(id_list, investors, invest,
							personPermalink, investor_info, type);
					ds.save(user);
				}else{
					//Only exists in Crunchbase, use another structure
					continue;
				}
			}
		}
		
//		HashMap<String,String> companies = AngelCrunch.getCrunchEntityList("companies");
//		for(Object key:companies.keySet()){
//			String permalink = key.toString();
//			if(personPermalink.containsKey(permalink)){
//				continue;
//			}else{
//				// First check if this person is in AngelList, first seperate
//				// name
//				// https://api.angel.co/1/search?query=reid+hoffman&type=User
//				String name = companies.get(key);
//				String[] spt = name.split(" ");
//				String search_name = "";
//				for (int i = 0; i < spt.length - 1; i++) {
//					search_name += spt[i];
//					search_name += "+";
//				}
//				search_name += spt[spt.length - 1];
//				String investor_info = AngelCrunch.searchAngelInvestor3(search_name);
//				if(investor_info !=null && !investor_info.equals("") &&!investor_info.isEmpty())
//				{
//					String type = "companies";
//					Investor user = createFin_org_InvestorObject(id_list, investors, invest,
//							personPermalink, investor_info, type);
//					ds.save(user);
//				}else{
//					//Only exists in Crunchbase, use another structure
//					continue;
//				}
//			}
//		}
		
		buff.close();
		return true;
	}
	

	protected static Investor createInvestorObject(HashMap<String, String> id_list, JSONObject investors,
			JSONArray invest, HashMap<String, String> personPermalink,
			String investor_info, String type) throws JSONException 
			{
		String investor_id = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.ID.getLabel().toString());
		System.out.println(investor_info);
		System.out.println(investor_id);
		String investor_name = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.NAME.getLabel().toString());
		// parse investor_name into first name and last name
		Pattern p = Pattern.compile("(\\S+)\\s(\\S+)",Pattern.DOTALL);
		Matcher m = p.matcher(investor_name);
		String fn = "";
		String ln = "";
		if (m.matches()){
			 fn += m.group(1);
			 ln += m.group(2);
		}
		String name = fn+"+"+ln;
		/*
		 * Here we can start using the name to locate records in the crunchbase.
		 * Wait, they have a permalink for each investor right? Just use this instead of 
		 * name. permalink:id
		 */
		String investor_bio = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.BIO.getLabel().toString());
		String investor_follower_count = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.FOLLOWER_COUNT.getLabel().toString());
		String investor_image = AngelCrunch.getInvestorfieldUrl(investor_info,InvestorEnum.INVESTOR_IMAGE.getLabel().toString());
		String linkedin_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.LINKEDIN_URL.getLabel().toString());
		String angellist_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.ANGLELIST_URL.getLabel().toString());
		String blog_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.BLOG_URL.getLabel().toString());
		String twitter_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.TWITTER_URL.getLabel().toString());
		String facebook_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.FB_URL.getLabel().toString());
		
		/*
		 * Add fields from Crunchbase, use investor_name as search field
		 */
		String crunch_person = AngelCrunch.getCrunchPerson(name);
		String permalink = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.PERMALINK.getLabel().toString());
		String first_name = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.FIRST_NAME.getLabel().toString());
		String last_name = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.LAST_NAME.getLabel().toString());
		String crunchbase_url = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.CRUNCHBASE_URL.getLabel().toString());
		String birthplace = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.BIRTHPLACE.getLabel().toString());
		String twitter_username = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.TWITTER_USERNAME.getLabel().toString());
		String born_year = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.BORN_YEAR.getLabel().toString());
		String born_month = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.BORN_MONTH.getLabel().toString());
		String born_day = AngelCrunch.getInvestorfield(crunch_person,InvestorEnum.BORN_DAY.getLabel().toString());
		
		/**
		 * Add permalink to HashMap, for future reference
		 */
		personPermalink.put(permalink, investor_id);
		
		//Second time call AngelList API+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		String start_up_role = AngelCrunch.getStartUpRole(investor_id);//get start-up role
		
		//Location not found, deal with such exceptions.
		JSONObject invest_info = AngelCrunch.parseToJSON(investor_info);
		JSONArray investor_locations = new JSONArray();
		if(invest_info != null)
		{
			if(invest_info.has(LocationEnum.LOCATION.getLabel().toString())){
				investor_locations = AngelCrunch.getInvestorfield_ja(investor_info,LocationEnum.LOCATION.getLabel().toString());
			}
		}
		
		System.out.println(start_up_role);
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
		each_investor.put("type", type);
		each_investor.put(InvestorEnum.COMPANY_COUNT.getLabel().toString(), company_count);
		each_investor.put(InvestorEnum.STARTUP_INVESTED.getLabel().toString(),startup_invested);
		each_investor.put(InvestorEnum.ID.getLabel().toString(), Integer.valueOf(investor_id));
		each_investor.put(InvestorEnum.NAME.getLabel().toString(),investor_name);
		each_investor.put(InvestorEnum.BIO.getLabel().toString(), investor_bio);
		each_investor.put(InvestorEnum.FOLLOWER_COUNT.getLabel().toString(), investor_follower_count);
		each_investor.put(InvestorEnum.INVESTOR_IMAGE.getLabel().toString(), investor_image);
		each_investor.put(InvestorEnum.ANGLELIST_URL.getLabel().toString(), angellist_url);
		each_investor.put(InvestorEnum.BLOG_URL.getLabel().toString(), blog_url);
		each_investor.put(InvestorEnum.TWITTER_URL.getLabel().toString(), twitter_url);
		each_investor.put(InvestorEnum.FB_URL.getLabel().toString(), facebook_url);
		each_investor.put(InvestorEnum.LINKEDIN_URL.getLabel().toString(), linkedin_url);
		each_investor.put(LocationEnum.LOCATION.getLabel().toString(), investor_locations);
		
		/**
		 * For Deepa, Here are some new fields added to Investor
		 */
		each_investor.put(InvestorEnum.PERMALINK.getLabel().toString(),permalink);
		each_investor.put(InvestorEnum.FIRST_NAME.getLabel().toString(),first_name);
		each_investor.put(InvestorEnum.LAST_NAME.getLabel().toString(),last_name);
		each_investor.put(InvestorEnum.CRUNCHBASE_URL.getLabel().toString(),crunchbase_url);
		each_investor.put(InvestorEnum.BIRTHPLACE.getLabel().toString(),birthplace);
		each_investor.put(InvestorEnum.TWITTER_USERNAME.getLabel().toString(),twitter_username);
		each_investor.put(InvestorEnum.BORN_YEAR.getLabel().toString(),born_year);
		each_investor.put(InvestorEnum.BORN_MONTH.getLabel().toString(),born_month);
		each_investor.put(InvestorEnum.BORN_DAY.getLabel().toString(),born_day);
		logger.debug(each_investor);
		
		/**
		 * Noted that for this stage of the project, we need only the
		 * each_investor JSONObject. Zoe you can start here to push 
		 * these investors into the MongoDB
		 */
		//This is for future reference. A giant JSONObject that contains all the investors.
		invest.put(each_investor);
		investors.put(InvestorEnum.INVESTOR_INFO.getLabel().toString(), invest);
		Investor user = new Investor(each_investor);
		return user;
	}
	
	protected static Investor createFin_org_InvestorObject(HashMap<String, String> id_list, JSONObject investors,
			JSONArray invest, HashMap<String, String> personPermalink,
			String investor_info, String type) throws JSONException 
			{
		String investor_id = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.ID.getLabel().toString());
		System.out.println(investor_info);
		System.out.println(investor_id);
		String investor_name = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.NAME.getLabel().toString());
		// parse investor_name into first name and last name
		String[] spt = investor_name.split(" ");
		String name = "";
		for (int i = 0; i < spt.length - 1; i++) {
			name += spt[i];
			name += "+";
		}
		name += spt[spt.length - 1];
		
		/*
		 * Here we can start using the name to locate records in the crunchbase.
		 * Wait, they have a permalink for each investor right? Just use this instead of 
		 * name. permalink:id
		 */
		String investor_follower_count = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.FOLLOWER_COUNT.getLabel().toString());
		String investor_image = AngelCrunch.getInvestorfieldUrl(investor_info,InvestorEnum.LOGO_URL.getLabel().toString());
		String angellist_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.ANGLELIST_URL.getLabel().toString());
		String blog_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.BLOG_URL.getLabel().toString());
		String twitter_url = AngelCrunch.getInvestorfield(investor_info,InvestorEnum.TWITTER_URL.getLabel().toString());
		
		String crunch_fin_org = "";
		if(type.equals("financial-organizations")){
			crunch_fin_org = AngelCrunch.getCrunchFinOrg(name);
		}else if(type.equals("companies")){
			crunch_fin_org = AngelCrunch.getCrunchCompanies(name);
		}else{
			crunch_fin_org = null;
		}

		String permalink = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.PERMALINK.getLabel().toString());
		String homepage_url = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.HOMEPAGE_URL.getLabel().toString());
		String crunchbase_url = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.CRUNCHBASE_URL.getLabel().toString());
		String twitter_username = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.TWITTER_USERNAME.getLabel().toString());
		String phone_number = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.PHONE_NUMBER.getLabel().toString());
		String description = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.DESCRIPTION.getLabel().toString());
		String email_address = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.EMAIL_ADDRESS.getLabel().toString());
		String number_of_employees = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.NUMBER_OF_EMPLOYEES.getLabel().toString());
		String founded_year = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.FOUNDED_YEAR.getLabel().toString());
		String founded_month = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.FOUNDED_MONTH.getLabel().toString());
		String founded_day = AngelCrunch.getInvestorfield(crunch_fin_org,InvestorEnum.FOUNDED_DAY.getLabel().toString());
		/**
		 * Add permalink to HashMap, for future reference
		 */
		personPermalink.put(permalink, investor_id);
		
		//Second time call AngelList API+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		String start_up_role = AngelCrunch.getStartUpRole(investor_id);//get start-up role
		
		//Location not found, deal with such exceptions.
		JSONObject invest_info = AngelCrunch.parseToJSON(investor_info);
		JSONArray location = new JSONArray();
		if(invest_info != null)
		{
			if(invest_info.has(LocationEnum.LOCATION.getLabel().toString())){
				location = AngelCrunch.getInvestorfield_ja(investor_info,LocationEnum.LOCATION.getLabel().toString());
			}
		}
		
//		System.out.println(start_up_role);
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
		each_investor.put("type", type);
		each_investor.put(InvestorEnum.COMPANY_COUNT.getLabel().toString(), company_count);
		each_investor.put(InvestorEnum.STARTUP_INVESTED.getLabel().toString(),startup_invested);
		each_investor.put(InvestorEnum.ID.getLabel().toString(), Integer.valueOf(investor_id));
		each_investor.put(InvestorEnum.NAME.getLabel().toString(),investor_name);
		each_investor.put(InvestorEnum.FOLLOWER_COUNT.getLabel().toString(), investor_follower_count);
		each_investor.put(InvestorEnum.INVESTOR_IMAGE.getLabel().toString(), investor_image);
		each_investor.put(InvestorEnum.ANGLELIST_URL.getLabel().toString(), angellist_url);
		each_investor.put(InvestorEnum.BLOG_URL.getLabel().toString(), blog_url);
		each_investor.put(InvestorEnum.TWITTER_URL.getLabel().toString(), twitter_url);
		each_investor.put(LocationEnum.LOCATION.getLabel().toString(), location);
		
		each_investor.put(InvestorEnum.PERMALINK.getLabel().toString(),permalink);
		each_investor.put(InvestorEnum.CRUNCHBASE_URL.getLabel().toString(),crunchbase_url);
		each_investor.put(InvestorEnum.TWITTER_USERNAME.getLabel().toString(),twitter_username);
		each_investor.put(InvestorEnum.HOMEPAGE_URL.getLabel().toString(),homepage_url);
		each_investor.put(InvestorEnum.PHONE_NUMBER.getLabel().toString(),phone_number);
		each_investor.put(InvestorEnum.DESCRIPTION.getLabel().toString(),description);
		each_investor.put(InvestorEnum.EMAIL_ADDRESS.getLabel().toString(),email_address);
		each_investor.put(InvestorEnum.NUMBER_OF_EMPLOYEES.getLabel().toString(),number_of_employees);
		each_investor.put(InvestorEnum.FOUNDED_YEAR.getLabel().toString(),founded_year);
		each_investor.put(InvestorEnum.FOUNDED_MONTH.getLabel().toString(),founded_month);
		each_investor.put(InvestorEnum.FOUNDED_DAY.getLabel().toString(),founded_day);
		
		logger.debug(each_investor);
		
		/**
		 * Noted that for this stage of the project, we need only the
		 * each_investor JSONObject. Zoe you can start here to push 
		 * these investors into the MongoDB
		 */
		//This is for future reference. A giant JSONObject that contains all the investors.
		invest.put(each_investor);
		investors.put(InvestorEnum.INVESTOR_INFO.getLabel().toString(), invest);
		Investor user = new Investor(each_investor);
		return user;
	}
}
