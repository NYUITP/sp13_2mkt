package com.secondmarket.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.Datastore;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.domain.Company;

public class InitialCompanyObj {
	protected static Logger logger = Logger.getLogger("core");

	public static boolean initialize(Datastore ds,
			HashMap<String, String> funding,
			HashMap<String, ArrayList<HashMap<Object, Object>>> round,
			HashMap<String, String> id_list, HashMap<String, Integer> counter,
			HashMap<String,String> PersonPermalink)
			throws JSONException {
		int cc = counter.get("count");
		for (Object key1 : id_list.keySet().toArray()) {
			if (cc > 950) {
				try {
					Thread.sleep(1000 * 60 * 60);
					counter.put("count", 0);
					cc = 0;
				} catch (InterruptedException ex) {
				}
			} else {
				// New JSONObject each_company. This is the JSONObject that will
				// be put into the MongoDB as Companies
				JSONObject each_company = new JSONObject();
				String key = key1.toString();
				String name = (id_list.get(key1)).toString();
				// Third time calling AngelList
				// API+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				String getFromAngel = AngelCrunch.getangelHTML(key);
				cc += 1;
				counter.put("count",cc);
				JSONObject jobj = AngelCrunch.parseToJSON(getFromAngel);
				String slug = AngelCrunch.getCrunchSlug(jobj);

				// Additional fields
				List<Integer> investorsInvested = AngelCrunch.getInvestorsForCompany(key);//gets part and current investors for given company
				
				int follower_count = AngelCrunch.getIntCompanyField(jobj,
						CompanyEnum.FOLLOWER_COUNT);
				int quality = AngelCrunch.getIntCompanyField(jobj,
						CompanyEnum.QUALITY);
				String prodDesc = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.PRODUCT_DESC);
				String concept = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.HIGH_CONCEPT);
				String logo_url = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.LOGO_URL);
				String company_url = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.COMPANY_URL);
				String twitter_url = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.TWITTER_URL);
				String blog_url = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.BLOG_URL);

				String angellist_url = AngelCrunch.getStringCompanyField(jobj,
						CompanyEnum.ANGLELIST_URL);
				JSONArray company_locations = null;
				if (jobj.has(LocationEnum.LOCATION.getLabel().toString())) {
					company_locations = AngelCrunch.getArrayCompanyField(jobj,
							LocationEnum.LOCATION.getLabel().toString());
				}

				if (!slug.isEmpty()) 
				{
					String crunchCompany = AngelCrunch.getcrunchHTML(slug);
					String total_funding = AngelCrunch
							.getCrunchTotalFund(crunchCompany);

					if (!total_funding.equals("$0")) {
						double total_fund = 0.0;
						each_company.put(CompanyEnum.FOLLOWER_COUNT.getLabel()
								.toString(), follower_count);
						each_company.put(CompanyEnum.QUALITY.getLabel()
								.toString(), quality);
						each_company.put(CompanyEnum.ANGLELIST_URL.getLabel()
								.toString(), angellist_url);
						each_company.put(CompanyEnum.ID.getLabel().toString(),
								key);
						each_company.put(
								CompanyEnum.NAME.getLabel().toString(), name);
						each_company.put(CompanyEnum.PRODUCT_DESC.getLabel()
								.toString(), prodDesc);
						each_company.put(CompanyEnum.HIGH_CONCEPT.getLabel()
								.toString(), concept);
						each_company.put(CompanyEnum.LOGO_URL.getLabel()
								.toString(), logo_url);
						each_company.put(CompanyEnum.COMPANY_URL.getLabel()
								.toString(), company_url);
						each_company.put(CompanyEnum.TWITTER_URL.getLabel()
								.toString(), twitter_url);
						each_company.put(CompanyEnum.BLOG_URL.getLabel()
								.toString(), blog_url);
						each_company.put(LocationEnum.LOCATION.getLabel()
								.toString(), company_locations);
						each_company.put(CompanyEnum.INVESTORS.getLabel()
								.toString(), investorsInvested);
						// Later: Process the total_funding amount using regular
						// expression and parse it to double.
						
						/*
						 * Here+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						 * getCrunchRoundFunding will have to do something more, say, matching 
						 * the investor name of each round funding with the ones in db.Investor
						 * First it need to match for name. if match, and single id, then use this id,
						 * if not, check if the company id exists in each of these investor's profile
						 * with their corresponding id. Will need to change the structure of Fund and
						 * fund_person, perhaps more. We'll come to that.
						 */
						each_company.put(CompanyEnum.FUNDING_ROUNDS.getLabel()
								.toString(), AngelCrunch
								.getCrunchRoundFunding(crunchCompany,PersonPermalink));  
						total_fund = AngelCrunch
								.getCrunchTotalFunding(crunchCompany);
						each_company.put(CompanyEnum.TOTAL_FUNDING.getLabel()
								.toString(), total_fund);
						AngelCrunch.crunchCompanyInfo(crunchCompany, key,
								funding, round);

						// if(!each_company.get("total_funding").equals("$0"))
						logger.debug(each_company);
						/**
						 * Here each_company is the JSONObject returned for each
						 * of the company. Zoe, you must push the JSONObject to
						 * MongoDB from HERE!!!!! This is really important! I
						 * have discarded all companies with $0 total funding
						 * amount and null fields!
						 */
						Company comp = new Company(each_company);
						ds.save(comp);
					}
				} else
					continue;
			}

		}
		return true;
	}
}
