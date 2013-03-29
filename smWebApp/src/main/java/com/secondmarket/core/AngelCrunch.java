package com.secondmarket.core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.secondmarket.domain.CompanyEnum;
import com.secondmarket.domain.FundEnum;
import com.secondmarket.domain.InvestorEnum;

public class AngelCrunch 
{	
	protected static Logger logger = Logger.getLogger("core"); 
	/***
	 * The method takes in a String of return content from the API and parse it into a JSONObject.
	 * @param input
	 * @return jobj
	 */
	public static JSONObject parseToJSON(String input){
		JSONObject jobj = null;
		try {
			jobj = new JSONObject(input);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj;	
	}
	
	/***
	 * This method returns the crunchbase url slug in correspondence to angellist record to be used for
	 * CrunchBase API query.
	 * @param jobj
	 * @return crunchbase slug in correspondence to angellist record.
	 * @throws JSONException
	 */
	public static String getCrunchSlug(JSONObject jobj){
		String url = "";
		String slug = "";
		try {
			url = jobj.getString(CompanyEnum.CRUNCHBASE_URL.getLabel().toString());
			Pattern p = Pattern.compile("http://www.crunchbase.com/company/(.*)",Pattern.DOTALL);
			Matcher match = p.matcher(url);
			if (match.matches()){
				 slug = match.group(1);
			}
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return slug;
	}
	
	/***
	 * Return the follower_count for each of the company
	 * @param jobj
	 * @return
	 */
	public static int getFollowerCount(JSONObject jobj){
		int f_count = 0;
		try {
			f_count = jobj.getInt(CompanyEnum.FOLLOWER_COUNT.getLabel().toString());
			
		} catch (JSONException e) {
		//	e.printStackTrace();
		}
		return f_count;
	}
	
	/***
	 * This method returns the quality of the company
	 * @param jobj
	 * @return
	 */
	public static int getQuality(JSONObject jobj){
		int q = 0;
		try {
			q = jobj.getInt(CompanyEnum.QUALITY.getLabel().toString());
			
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return q;
	}
	
	/***
	 * Returns the angellist url for each_company
	 * @param jobj
	 * @return
	 */
	public static String getAngelListUrl(JSONObject jobj){
		String url = null;
		try {
			url = jobj.getString(CompanyEnum.ANGLELIST_URL.getLabel().toString());
			
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return url;
	}
	
	/***
	 * 
	 * @param slug is the url slug of the investor
	 * @return result is a json format result of investor information. Use this information to set-up a library of company names.
	 */
	public static String searchAngelInvestor(String slug){
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try{
			url = new URL("https://api.angel.co/1/users/search?slug="+slug);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine())!=null){
				result += line;
//				result += "\n";
			}
			rd.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * This method returns the specific field associated with the investor
	 * @param investor
	 * @param field
	 * @return the desired string according to the field
	 */
	public static String getfield(String investor, String field){
		JSONObject jj = null;
		String result = "";
		try {
			jj = new JSONObject(investor);
			result += jj.get(field);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * Use investor_id to get their startup roles
	 * @param investor_id
	 * @return result is a string of startup role record
	 */
	public static String getStartUpRole(String investor_id){
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try{
			url = new URL("https://api.angel.co/1/users/"+investor_id+"/startups");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine())!=null){
				result += line;
			}
			rd.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * Extract company ids and names from start_up_role, and
	 * put them into the HashMap
	 * Use RegEx to match the url slug from angellist url. use this slug to match the crunchbase api query.
	 * @param start_up_role
	 * @param companySlugList
	 */
	public static void hashCompanyList(String start_up_role, HashMap<String, String> id_list){
		JSONObject startupRole = null;
		try {
			startupRole = new JSONObject(start_up_role);
//			System.out.println(startupRole);
			JSONArray st_arr = startupRole.getJSONArray(InvestorEnum.STARTUP_ROLES.getLabel().toString());

			for (int index = 0; index<st_arr.length(); ++index){
				JSONObject each = st_arr.getJSONObject(index);
				JSONObject company = each.getJSONObject(InvestorEnum.STARTUP.getLabel().toString());
				
				Integer id = company.getInt(CompanyEnum.ID.getLabel().toString());
				String id2 = id.toString();
				String name = company.getString(CompanyEnum.NAME.getLabel().toString());
				String url = company.getString(CompanyEnum.ANGLELIST_URL.getLabel().toString());
				Pattern p = Pattern.compile("https://angel.co/(.*)",Pattern.DOTALL);
				Matcher match = p.matcher(url);
				@SuppressWarnings("unused")
				String slug2 = "";
				if (match.matches()){
					 slug2 += match.group(1);
				}
				/*
				 * Put the id and name into the HashMap
				 */
				id_list.put(id2,name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/***
	 * getangelHTML(String id) takes in a String of company id and get the web content of this specific company. 
	 * @param id
	 * @return JSON format content
	 */
	public static String getangelHTML(String id){
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try{
			url = new URL("https://api.angel.co/1/startups/"+id);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine())!=null){
				result += line;
			}
			rd.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * Get matching data from CrunchBase
	 * @param company_name
	 * @return
	 * @throws IOException 
	 */
	public static String getcrunchHTML(String company_name){
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try{
			url = new URL("http://api.crunchbase.com/v/1/company/"+company_name+".js?api_key=m97aznucw5d57wk9m5a94ekp");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine())!=null){
				result += line;
//				result += "\n";
			}
			rd.close();
		} catch(Exception e){
			//e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * id_funding modifies the total amount of fund raised for this company, use id as key
	 * id_round specifies the amount of funding in each round, use id as key, an ArrayList of 
	 * HashMaps that specifies each round of funding as value.
	 * @param crunchCompany
	 * @param id
	 * @param id_funding
	 * @param id_round
	 */
	public static void crunchCompanyInfo(String crunchCompany, String id, HashMap<String, String> id_funding, HashMap<String, ArrayList<HashMap<Object, Object>>> id_round){
		JSONObject company = null;
		try {
			company = new JSONObject(crunchCompany);
			String funding = company.getString(CompanyEnum.TOTAL_MONEY_RAISED.getLabel().toString());
			JSONArray st_arr = company.getJSONArray(CompanyEnum.FUNDING_ROUNDS.getLabel().toString());
			
			ArrayList<HashMap<Object, Object>> all = new ArrayList<HashMap<Object, Object>>();
			for (int index = 0; index<st_arr.length(); ++index){
				JSONObject each = st_arr.getJSONObject(index);
				HashMap<Object, Object> temp = new HashMap<Object, Object>();
				temp.put(each.get(FundEnum.ROUND_CODE.getLabel().toString()), each.get(FundEnum.RAISED_AMOUNT.getLabel().toString()));
				all.add(temp);
			}
			id_funding.put(id, funding);
			id_round.put(id,all);
		} catch (JSONException e) {
			//e.printStackTrace();
		}
	}
	
	/***
	 * This method takes in the String of crunchbase and return the total funding
	 * of this company
	 * @param crunchCompany
	 * @return The total funding of each company available from crunchBase
	 */
	public static String getCrunchTotalFund(String crunchCompany){
		JSONObject company = null;
		String funding = "$0";
		try {
			company = new JSONObject(crunchCompany);
			funding = company.getString(CompanyEnum.TOTAL_MONEY_RAISED.getLabel().toString());
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return funding;
	}
	
	/***
	 * Haven't 
	 * @param crunchCompany
	 * @return
	 */
	public static String getCrunchURL(String crunchCompany){
		JSONObject company = null;
		String url = null;
		try {
			company = new JSONObject(crunchCompany);
			url = company.getString(CompanyEnum.TOTAL_MONEY_RAISED.getLabel().toString());
			
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return url;
	}
	
	/***
	 * This method will return a JSONArray created to contain all
	 * important fields of interest regarding the funding of each
	 * round. Specific fields include:
	 * 	round_code:
	 * 	raised_amount:
	 * 	funded_year:
	 * 	funded_month:
	 * 	funded_date:
	 * 	fund_company: if this round of funding is from a company
	 * 	fund_financial_org: if this round of funding is from a financial organization
	 * 	fund_person: if this round of funding is from a person investor
	 * Here I cheated a little bit about the last three fields. I
	 * simply put the original data into the new object. For ranking
	 * purpose, all we need is the names and permalink. We might consider
	 * to import the pictures later.
	 * @param crunchCompany
	 * @return A JSONArray of each round of funding
	 */
	public static JSONArray getCrunchRoundFunding(String crunchCompany){
		JSONArray round_funding = new JSONArray();
		JSONObject company = null;
		
		HashMap<String, List<Double>> hm_dollar = new HashMap<String, List<Double>>();
		try {
			company = new JSONObject(crunchCompany);
			JSONArray st_arr = company.getJSONArray(CompanyEnum.FUNDING_ROUNDS.getLabel().toString());
			
			for (int index = 0; index<st_arr.length(); ++index){
				//each round has a JSONObject that contains all stuff
				JSONObject each_round = new JSONObject();
				JSONObject each = st_arr.getJSONObject(index);// each is a JSONObject
				String round_code = each.getString(FundEnum.ROUND_CODE.getLabel().toString());
				double raised_amount = each.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString());
				int funded_year = each.getInt(FundEnum.YEAR.getLabel().toString());
				int funded_month = each.getInt(FundEnum.MONTH.getLabel().toString());
				int funded_day = each.getInt(FundEnum.DAY.getLabel().toString());
				if (!hm_dollar.containsKey(round_code)){
					List<Double> lst = new ArrayList<Double>();
					lst.add(raised_amount);
					hm_dollar.put(round_code, lst);
				}else{
					hm_dollar.get(round_code).add(raised_amount);
				}
				each_round.put(FundEnum.ROUND_CODE.getLabel().toString(), round_code);
				each_round.put(FundEnum.RAISED_AMOUNT.getLabel().toString(), raised_amount);
				each_round.put(FundEnum.YEAR.getLabel().toString(),funded_year);
				each_round.put(FundEnum.MONTH.getLabel().toString(),funded_month);
				each_round.put(FundEnum.DAY.getLabel().toString(),funded_day);
				//get all the investments in each round
				JSONArray investments = each.getJSONArray("investments");
				JSONArray fund_company = new JSONArray();
				JSONArray fund_financial_org = new JSONArray();
				JSONArray fund_person = new JSONArray();
				for (int index2 = 0; index2<investments.length(); ++index2){
					JSONObject each_investment = investments.getJSONObject(index2);
					if(!each_investment.isNull("company"))
						fund_company.put(each_investment.get("company"));
					if(!each_investment.isNull("financial_org"))
						fund_financial_org.put(each_investment.get("financial_org"));
					if(!each_investment.isNull("person"))	
						fund_person.put(each_investment.get("person"));
					
				}
				each_round.put("fund_company",fund_company);
				each_round.put("fund_financial_org", fund_financial_org);
				each_round.put("fund_person",fund_person);
				round_funding.put(each_round);
			}
//			id_funding.put(id, funding);
//			id_round.put(id,al);
		} catch (JSONException e) {
			//e.printStackTrace();
		}
//		System.out.println(round_funding);
		return round_funding;
	}
}