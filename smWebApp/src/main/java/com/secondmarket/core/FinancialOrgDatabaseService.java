package com.secondmarket.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.Datastore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.CrunchbaseNamespace;
import com.secondmarket.common.Financial_OrgEnum;
import com.secondmarket.common.FundEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.common.RandomIdGenerator;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Location;

public class FinancialOrgDatabaseService 
{
protected static Logger logger = Logger.getLogger("core");
	
	public static void populateFinancialOrgCollection(Datastore ds, String financialOrgPermalink)
	{
		boolean isCollectionExist = isCollectionExist(ds, financialOrgPermalink);
		if(!isCollectionExist)
		{
			String finOrgDataFromCrunch = AngelCrunchDataService.getObjectFromCrunchbaseUsingPermalink
					(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString(), financialOrgPermalink);
			
			if(!finOrgDataFromCrunch.equals(""))
			{
				JSONObject finOrgObjectFromCrunch = AngelCrunchDataService.parseToJSON(finOrgDataFromCrunch);
				
				if(finOrgObjectFromCrunch != null)
				{
					try 
					{
						String name = finOrgObjectFromCrunch.get(Financial_OrgEnum.NAME.getLabel().toString()).toString().trim();
						String nameForSearch = name.replace(" ", "+");
						String finOrgDataFromAngel = AngelCrunchDataService.getObjectFromAngelUsingSearchQuery("Startup", nameForSearch);
						JSONArray finOrgArrayFromAngel = AngelCrunchDataService.parseToJSONArray(finOrgDataFromAngel);
					
						createAndPersistFinOrgObjects(ds, finOrgObjectFromCrunch, finOrgArrayFromAngel);
					}
					catch (JSONException e) {
						logger.warn("Name field not found in company object" + financialOrgPermalink);
					}
				}
			}
		}
	}

	private static void createAndPersistFinOrgObjects(Datastore ds,
			JSONObject finOrgObjectFromCrunch, JSONArray finOrgArrayFromAngel) throws JSONException
	{
		Financial_Org finOrg = new Financial_Org();
		JSONObject jobj = getAngelMatch(finOrgObjectFromCrunch, finOrgArrayFromAngel);
		if(jobj != null)
		{
			try 
			{
				String id = jobj.get(Financial_OrgEnum.ID.getLabel().toString()).toString();
				String angelData = AngelCrunchDataService.getObjectFromAngelUsingId("startups", id);
				if(!angelData.equals(""))
				{
					JSONObject angelObj = AngelCrunchDataService.parseToJSON(angelData);
					if(angelObj != null)
					{
						setAngelFieldsFromAngelObject(finOrg, angelObj);
					}
					else
					{
						setAngelFieldsFromCrunchbase(finOrg, finOrgObjectFromCrunch);
					}
				}
				else
				{
					setAngelFieldsFromCrunchbase(finOrg, finOrgObjectFromCrunch);
				}
				
			} catch (JSONException e) {
				
				logger.warn("Couldn't parse array to json object");
			}
		}
		else
		{
			setAngelFieldsFromCrunchbase(finOrg, finOrgObjectFromCrunch);
		}
		
		setCrunchFieldsFromCrunchbase(finOrg, finOrgObjectFromCrunch);
		ds.save(finOrg);
	}

	private static void setCrunchFieldsFromCrunchbase(Financial_Org finOrg, JSONObject finOrgObjectFromCrunch) 
	{
		try 
		{
			finOrg.setFoundInCrunchbase(true);
			finOrg.setPermalink(finOrgObjectFromCrunch.get(Financial_OrgEnum.PERMALINK.getLabel().toString()).toString());
			finOrg.setCrunchbase_url(finOrgObjectFromCrunch.get(Financial_OrgEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
			finOrg.setOverview(finOrgObjectFromCrunch.get(Financial_OrgEnum.OVERVIEW.getLabel().toString()).toString());
			
			setInvestmentInfo(finOrg, finOrgObjectFromCrunch);
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object");
		}
	}

	private static void setInvestmentInfo(Financial_Org finOrg, JSONObject finOrgObjectFromCrunch)
	{
		try 
		{
			JSONArray investments = finOrgObjectFromCrunch.getJSONArray(Financial_OrgEnum.INVESTMENTS.getLabel().toString());
			List<Fund> funds = new ArrayList<Fund>();
			Set<String> allCompaniesInvestedIn = new HashSet<String>();
			
			for(int index = 0; index < investments.length() ; index++)
			{
				JSONObject fund_round = investments.getJSONObject(index).getJSONObject(Financial_OrgEnum.FUND_ROUNDS.getLabel().toString());
				
				Fund fund = new Fund();
				fund.setRound_code(fund_round.get(FundEnum.ROUND_CODE.getLabel().toString()).toString());
				if(!fund_round.get(FundEnum.RAISED_AMOUNT.getLabel().toString()).toString().equals("null"))
				{
					fund.setRaised_amount(fund_round.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString()));
				}
				if(!fund_round.get(FundEnum.YEAR.getLabel().toString()).toString().equals("null"))
				{
					fund.setFunded_year(fund_round.getInt(FundEnum.YEAR.getLabel().toString()));
				}
				if(!fund_round.get(FundEnum.MONTH.getLabel().toString()).toString().equals("null"))
				{
					fund.setFunded_month(fund_round.getInt(FundEnum.MONTH.getLabel().toString()));
				}
				if(!fund_round.get(FundEnum.DAY.getLabel().toString()).toString().equals("null"))
				{
					fund.setFunded_day(fund_round.getInt(FundEnum.DAY.getLabel().toString()));
				}
				
				JSONObject companyObj = fund_round.getJSONObject(FundEnum.COMPANY.getLabel().toString());
				Company company = new Company();
				if(companyObj != null)
				{
					company.setName(companyObj.get(CompanyEnum.NAME.getLabel().toString()).toString());
					company.setPermalink(companyObj.get(CompanyEnum.PERMALINK.getLabel().toString()).toString());
				}
				fund.addCompany(company);
				allCompaniesInvestedIn.add(company.getPermalink());
				funds.add(fund);
			}
			
			List<String> companiesInvestedIn = new ArrayList<String>();
			companiesInvestedIn.addAll(allCompaniesInvestedIn);
			finOrg.setCompaniesInvestedIn(companiesInvestedIn);
			finOrg.setCompany_count(allCompaniesInvestedIn.size());
			finOrg.setFund_info(funds);
			
		} catch (JSONException e) {
			logger.warn("Failed while getting funding rounds from json object - ");
		}
	}

	private static void setAngelFieldsFromCrunchbase(Financial_Org finOrg, JSONObject finOrgObjectFromCrunch) 
	{
		try 
		{
			finOrg.setId(RandomIdGenerator.getRandomId());
			finOrg.setName(finOrgObjectFromCrunch.get(Financial_OrgEnum.NAME.getLabel().toString()).toString());
			finOrg.setAngellist_url("");
			finOrg.setLogo_url("");
			finOrg.setFollower_count(0);
			finOrg.setCompany_url(finOrgObjectFromCrunch.get(Financial_OrgEnum.HOME_PAGE_URL.getLabel().toString()).toString());
			if(!finOrgObjectFromCrunch.get(Financial_OrgEnum.TWITTER_USERNAME.getLabel().toString()).toString().equals("null"))
			{
				String twitter_url = "https://twitter.com/"+finOrgObjectFromCrunch.get(Financial_OrgEnum.TWITTER_USERNAME.getLabel().toString()).toString();
				finOrg.setTwitter_url(twitter_url);
			}
			else
			{
				finOrg.setTwitter_url("https://twitter.com/");
			}
			
			JSONArray locations = finOrgObjectFromCrunch.getJSONArray(Financial_OrgEnum.OFFICES.getLabel().toString());
			for(int index = 0; index < locations.length(); index++)
			{
				Location location = new Location(locations.getJSONObject(index), "city");
				finOrg.addLocation(location);
			}
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object - ");
		}
	}

	private static void setAngelFieldsFromAngelObject(Financial_Org finOrg, JSONObject angelObj) 
	{
		try 
		{
			finOrg.setFoundInAngelList(true);
			
			finOrg.setId(angelObj.getInt(Financial_OrgEnum.ID.getLabel().toString()));
			finOrg.setName(angelObj.get(Financial_OrgEnum.NAME.getLabel().toString()).toString());
			finOrg.setAngellist_url(angelObj.get(Financial_OrgEnum.ANGLELIST_URL.getLabel().toString()).toString());
			finOrg.setLogo_url(angelObj.get(Financial_OrgEnum.LOGO_URL.getLabel().toString()).toString());
			finOrg.setFollower_count(angelObj.getInt(Financial_OrgEnum.FOLLOWER_COUNT.getLabel().toString()));
			finOrg.setCompany_url(angelObj.get(Financial_OrgEnum.COMPANY_URL.getLabel().toString()).toString());
			if(!angelObj.get(Financial_OrgEnum.TWITTER_URL.getLabel().toString()).toString().equals("null"))
			{
				finOrg.setTwitter_url(angelObj.get(Financial_OrgEnum.TWITTER_URL.getLabel().toString()).toString());
			}
			else
			{
				finOrg.setTwitter_url("https://twitter.com/");
			}
			JSONArray locations = angelObj.getJSONArray(Financial_OrgEnum.LOCATIONS.getLabel().toString());
			for(int index = 0; index < locations.length(); index++)
			{
				Location location = new Location(locations.getJSONObject(index));
				finOrg.addLocation(location);
			}
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object - ");
		}
	}

	private static JSONObject getAngelMatch(JSONObject finOrgObjectFromCrunch, JSONArray finOrgArrayFromAngel) throws JSONException 
	{
		JSONObject jobj = null;
		if(finOrgArrayFromAngel != null && finOrgArrayFromAngel.length() == 1)
		{
			jobj = finOrgArrayFromAngel.getJSONObject(0);
		}
		else if(finOrgArrayFromAngel != null)
		{
			for(int index = 0; index < finOrgArrayFromAngel.length() ; index++)
			{
				JSONObject finOrg = finOrgArrayFromAngel.getJSONObject(index);
				String nameAngel = finOrg.get(Financial_OrgEnum.NAME.getLabel().toString()).toString().trim();
				String nameCrunch = finOrgObjectFromCrunch.get(Financial_OrgEnum.NAME.getLabel().toString()).toString().trim();
				if(nameAngel.equalsIgnoreCase(nameCrunch))
				{
					jobj = finOrg;
					break;
				}
			}
		}
		return jobj;
	}

	private static boolean isCollectionExist(Datastore ds, String permalink) 
	{	
		boolean isCollectionExist = false;
		
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.FINANCIAL_ORG.getLabel().toString());
		DBObject doc = new BasicDBObject(); 
		doc.put(Financial_OrgEnum._ID.getLabel().toString(), permalink); 
		DBObject dbObject = coll.findOne(doc); 
		if(dbObject != null)
		{
			isCollectionExist = true;
			logger.debug("Financial org for this permalink already exists in database - " + permalink);
		}
		return isCollectionExist;
	}
}
