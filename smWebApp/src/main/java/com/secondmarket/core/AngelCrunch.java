package com.secondmarket.core;
import java.io.* ;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.* ;
import java.util.* ;

import org.json.*;

public class AngelCrunch {
	/***
	 * The method takes in a String of return content from the API and parse it into a JSONObject.
	 * @param input
	 * @return jobj
	 */
	public JSONObject parseToJSON(String input){
		JSONObject jobj = null;
		try {
			jobj = new JSONObject(input);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
	public String getCrunchSlug(JSONObject jobj){
		String url = "";
		String slug = "";
		try {
			url = jobj.getString("crunchbase_url");
			Pattern p = Pattern.compile("http://www.crunchbase.com/company/(.*)",Pattern.DOTALL);
			Matcher match = p.matcher(url);
			if (match.matches()){
				 slug = match.group(1);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		return slug;
	}
	
	/***
	 * Return the follower_count for each of the company
	 * @param jobj
	 * @return
	 */
	public int getFollowerCount(JSONObject jobj){
		int f_count = 0;
		try {
			f_count = jobj.getInt("follower_count");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		return f_count;
	}
	
	/***
	 * This method returns the quality of the company
	 * @param jobj
	 * @return
	 */
	public int getQuality(JSONObject jobj){
		int q = 0;
		try {
			q = jobj.getInt("follower_count");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		return q;
	}
	
	/***
	 * Returns the angellist url for each_company
	 * @param jobj
	 * @return
	 */
	public String getAngelListUrl(JSONObject jobj){
		String url = null;
		try {
			url = jobj.getString("angellist_url");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		return url;
	}
	
	/***
	 * 
	 * @param slug is the url slug of the investor
	 * @return result is a json format result of investor information. Use this information to set-up a library of company names.
	 */
	public String searchAngelInvestor(String slug){
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

//	/***
//	 * Get the id of investor (Now use this to get all information about the investor!
//	 * @param investor
//	 * @return result is the id of the investor
//	 */
//	public String getID(String investor){
//		JSONObject jj = null;// = new JSONObject(str1);
//		String result = "";
//		try {
//			jj = new JSONObject(investor);
//			result += jj.get("id");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/***
	 * This method returns the specific field associated with the investor
	 * @param investor
	 * @param field
	 * @return the desired string according to the field
	 */
	public String getfield(String investor, String field){
		JSONObject jj = null;// = new JSONObject(str1);
		String result = "";
		try {
			jj = new JSONObject(investor);
			result += jj.get(field);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
//	public String getNAME(String investor){
//		JSONObject jj = null;// = new JSONObject(str1);
//		String result = "";
//		try {
//			jj = new JSONObject(investor);
//			result += jj.get("name");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/***
	 * Get company information by id
	 * @param company
	 */
	public void companyINFO(String id){
		JSONObject jj = null;// = new JSONObject(str1);
		String ID = id;
		String followerCnt = "";
		String Quality = "";
		String Name = "";
		try {
			jj = new JSONObject(id);
			Name += jj.get("name");
			Quality += jj.get("quality");
			followerCnt += jj.get("follower_count");
			/*
			 * Some other things like all kinds of urls could be added here.
			 */
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * Use investor_id to get their startup roles
	 * @param investor_id
	 * @return result is a string of startup role record
	 */
	public String getStartUpRole(String investor_id){
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
	public void hashCompanyList(String start_up_role,HashMap id_list){
		JSONObject startupRole = null;// = new JSONObject(str1);
		try {
			startupRole = new JSONObject(start_up_role);
//			System.out.println(startupRole);
			JSONArray st_arr = startupRole.getJSONArray("startup_roles");

			for (int index = 0; index<st_arr.length(); ++index){
				JSONObject each = st_arr.getJSONObject(index);
				JSONObject company = each.getJSONObject("startup");
				
				Integer id = company.getInt("id");
				String id2 = id.toString();
				String name = company.getString("name");
				String url = company.getString("angellist_url");
//				System.out.println(url);
				Pattern p = Pattern.compile("https://angel.co/(.*)",Pattern.DOTALL);
				Matcher match = p.matcher(url);
				String slug2 = "";
				if (match.matches()){
					 slug2 += match.group(1);
				}
				/*
				 * Put the id and name into the HashMap
				 */
//				companyList.put(id2, name);
				id_list.put(id2,name);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/***
	 * getangelHTML(String id) takes in a String of company id and get the web content of this specific company. 
	 * @param id
	 * @return JSON format content
	 */
	public String getangelHTML(String id){
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
	public String getcrunchHTML(String company_name){
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
//			e.printStackTrace();
//			System.out.println(company_name);
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
	public void crunchCompanyInfo(String crunchCompany, String id, HashMap id_funding, HashMap id_round){
		JSONObject company = null;// = new JSONObject(str1);
		try {
			company = new JSONObject(crunchCompany);
//			System.out.println(company);
			String funding = company.getString("total_money_raised");
			JSONArray st_arr = company.getJSONArray("funding_rounds");
			
			ArrayList al = new ArrayList();
//			System.out.println(st_arr);
			for (int index = 0; index<st_arr.length(); ++index){
				JSONObject each = st_arr.getJSONObject(index);
//				System.out.println(each); // each is a JSONObject
				HashMap temp = new HashMap();
//				temp.put(rc, am);
				temp.put(each.get("round_code"), each.get("raised_amount"));
				al.add(temp);
			}
			id_funding.put(id, funding);
			id_round.put(id,al);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	/***
	 * This method takes in the String of crunchbase and return the total funding
	 * of this company
	 * @param crunchCompany
	 * @return The total funding of each company available from crunchBase
	 */
	public String getCrunchTotalFund(String crunchCompany){
		JSONObject company = null;// = new JSONObject(str1);
		String funding = "$0";
		try {
			company = new JSONObject(crunchCompany);
//			System.out.println(company);
			funding = company.getString("total_money_raised");
			
//			JSONArray st_arr = company.getJSONArray("funding_rounds");
//			
//			ArrayList al = new ArrayList();
////			System.out.println(st_arr);
//			for (int index = 0; index<st_arr.length(); ++index){
//				JSONObject each = st_arr.getJSONObject(index);
////				System.out.println(each); // each is a JSONObject
//				HashMap temp = new HashMap();
////				temp.put(rc, am);
//				temp.put(each.get("round_code"), each.get("raised_amount"));
//				al.add(temp);
//			}
//			id_funding.put(id, funding);
//			id_round.put(id,al);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return funding;
	}
	
	/***
	 * Haven't 
	 * @param crunchCompany
	 * @return
	 */
	public String getCrunchURL(String crunchCompany){
		JSONObject company = null;// = new JSONObject(str1);
		String url = null;
		try {
			company = new JSONObject(crunchCompany);
//			System.out.println(company);
			url = company.getString("total_money_raised");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return url;
	}
	
	
//	public HashMap getCrunchRoundFunding(String crunchCompany){
//		JSONObject company = null;// = new JSONObject(str1);
//		String funding = null;
//		HashMap<String, List<Double>> hm_dollar = new HashMap();
//		try {
//			company = new JSONObject(crunchCompany);
//			JSONArray st_arr = company.getJSONArray("funding_rounds");
//			
//			for (int index = 0; index<st_arr.length(); ++index){
//				JSONObject each = st_arr.getJSONObject(index);// each is a JSONObject
//				String round_code = each.getString("round_code");
//				double raised_amount = each.getDouble("raised_amount");
//				if (!hm_dollar.containsKey(round_code)){
//					List lst = new ArrayList();
//					lst.add(raised_amount);
//					hm_dollar.put(round_code, lst);
//				}else{
//					hm_dollar.get(round_code).add(raised_amount);
//				}
//				
//			}
////			id_funding.put(id, funding);
////			id_round.put(id,al);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
////			e.printStackTrace();
//		}
//		return hm_dollar;
//	}
	
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
	public JSONArray getCrunchRoundFunding(String crunchCompany){
		JSONArray round_funding = new JSONArray();
		JSONObject company = null;// = new JSONObject(str1);
		String funding = null;
		HashMap<String, List<Double>> hm_dollar = new HashMap();
		try {
			company = new JSONObject(crunchCompany);
			JSONArray st_arr = company.getJSONArray("funding_rounds");
			
			for (int index = 0; index<st_arr.length(); ++index){
				//each round has a JSONObject that contains all stuff
				JSONObject each_round = new JSONObject();
				JSONObject each = st_arr.getJSONObject(index);// each is a JSONObject
				String round_code = each.getString("round_code");
//				System.out.println(round_code);
				double raised_amount = each.getDouble("raised_amount");
				int funded_year = each.getInt("funded_year");
				int funded_month = each.getInt("funded_month");
				int funded_day = each.getInt("funded_day");
				if (!hm_dollar.containsKey(round_code)){
					List lst = new ArrayList();
					lst.add(raised_amount);
					hm_dollar.put(round_code, lst);
				}else{
					hm_dollar.get(round_code).add(raised_amount);
				}
				each_round.put("round_code", round_code);
//				System.out.println(each_round);
				each_round.put("raised_amount", raised_amount);
				each_round.put("funded_year",funded_year);
				each_round.put("funded_month",funded_month);
				each_round.put("funded_day",funded_day);
//				System.out.println(each_round);
				//get all the investments in each round
				JSONArray investments = each.getJSONArray("investments");
//				System.out.println(investments);
				JSONArray fund_company = new JSONArray();
				JSONArray fund_financial_org = new JSONArray();
				JSONArray fund_person = new JSONArray();
				for (int index2 = 0; index2<investments.length(); ++index2){
					JSONObject each_investment = investments.getJSONObject(index2);
//					System.out.println(each_investment.get("person"));
//					JSONObject ccc = (JSONObject) each_investment.get("company");
					if(!each_investment.isNull("company"))
						fund_company.put(each_investment.get("company"));
					if(!each_investment.isNull("financial_org"))
						fund_financial_org.put(each_investment.get("financial_org"));
					if(!each_investment.isNull("person"))	
						fund_person.put(each_investment.get("person"));
					
				}
//				System.out.println(fund_company);
//				System.out.println(fund_financial_org);
//				System.out.println(fund_person);
				each_round.put("fund_company",fund_company);
				each_round.put("fund_financial_org", fund_financial_org);
				each_round.put("fund_person",fund_person);
//				System.out.println(each_round);
				round_funding.put(each_round);
			}
//			id_funding.put(id, funding);
//			id_round.put(id,al);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
//		System.out.println(round_funding);
		return round_funding;
	}
	
	
	
}