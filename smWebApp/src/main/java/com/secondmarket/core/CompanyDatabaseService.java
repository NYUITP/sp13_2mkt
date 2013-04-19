package com.secondmarket.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.common.RandomIdGenerator;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Location;

public class CompanyDatabaseService 
{
	protected static Logger logger = Logger.getLogger("core");
	private Map<String, String> permalinkMap = new HashMap<String, String>();
	
	public Map<String, String> populateCompanyCollection(Datastore ds, List<String> companyPermalinks)
	{
		for(String permalink : companyPermalinks)
		{
			populateSingleCompanyCollection(ds, permalink);
		}
		return permalinkMap;
	}

	protected void populateSingleCompanyCollection(Datastore ds, String permalink)
	{
		boolean isCollectionExist = isCollectionExist(ds, permalink);
		if(!isCollectionExist)
		{
			logger.debug("Working on permalink - " + permalink);
			String companyDataFromCrunch = AngelCrunchDataService.getObjectFromCrunchbaseUsingPermalink
					(CrunchbaseNamespace.COMPANY.getLabel().toString(), permalink);
			
			if(!companyDataFromCrunch.equals(""))
			{
				JSONObject companyObjectFromCrunch = AngelCrunchDataService.parseToJSON(companyDataFromCrunch);
				
				if(companyObjectFromCrunch != null)
				{
					try 
					{
						String name = companyObjectFromCrunch.get(CompanyEnum.NAME.getLabel().toString()).toString().trim();
						String nameForSearch = name.replace(" ", "+");
						String companyDataFromAngel = AngelCrunchDataService.getObjectFromAngelUsingSearchQuery("Startup", nameForSearch);
						JSONArray companyArrayFromAngel = AngelCrunchDataService.parseToJSONArray(companyDataFromAngel);
					
						createAndPersistCompanyObjects(ds, companyObjectFromCrunch, companyArrayFromAngel);
					}
					catch (JSONException e) {
						logger.warn("Name field not found in company object" + permalink);
					}
				}
			}
		}
	}
	
	private void createAndPersistCompanyObjects(Datastore ds, JSONObject companyObjectFromCrunch, 
			JSONArray companyArrayFromAngel) throws JSONException 
	{
		Company company = new Company();
		JSONObject jobj = getAngelMatch(companyObjectFromCrunch, companyArrayFromAngel);
		if(jobj != null)
		{
			try 
			{
				String id = jobj.get(CompanyEnum.ID.getLabel().toString()).toString();
				String angelData = AngelCrunchDataService.getObjectFromAngelUsingId("startups", id);
				if(!angelData.equals(""))
				{
					JSONObject angelObj = AngelCrunchDataService.parseToJSON(angelData);
					if(angelObj != null)
					{
						setAngelFieldsFromAngelObject(company, angelObj);
					}
					else
					{
						setAngelFieldsFromCrunchbase(company, companyObjectFromCrunch);
					}
				}
				else
				{
					setAngelFieldsFromCrunchbase(company, companyObjectFromCrunch);
				}
				
			} catch (JSONException e) {
				
				logger.warn("Couldn't parse array to json object");
			}
		}
		else
		{
			setAngelFieldsFromCrunchbase(company, companyObjectFromCrunch);
		}
		
		setCrunchFieldsFromCrunchbase(company, companyObjectFromCrunch);
		ds.save(company);
	}

	private static JSONObject getAngelMatch(JSONObject companyObjectFromCrunch, 
			JSONArray companyArrayFromAngel) throws JSONException 
	{
		JSONObject jobj = null;
		if(companyArrayFromAngel != null && companyArrayFromAngel.length() == 1)
		{
			jobj = companyArrayFromAngel.getJSONObject(0);
		}
		else if(companyArrayFromAngel != null)
		{
			for(int index = 0; index < companyArrayFromAngel.length() ; index++)
			{
				JSONObject company = companyArrayFromAngel.getJSONObject(index);
				String nameAngel = company.get(CompanyEnum.NAME.getLabel().toString()).toString().trim();
				String nameCrunch = companyObjectFromCrunch.get(CompanyEnum.NAME.getLabel().toString()).toString().trim();
				if(nameAngel.equalsIgnoreCase(nameCrunch))
				{
					jobj = company;
					break;
				}
			}
		}
		return jobj;
	}

	private void setCrunchFieldsFromCrunchbase(Company company,JSONObject companyObjectFromCrunch)
	{
		try 
		{
			company.setFoundInCrunchbase(true);
			company.setPermalink(companyObjectFromCrunch.get(CompanyEnum.PERMALINK.getLabel().toString()).toString());
			company.setCrunchbase_url(companyObjectFromCrunch.get(CompanyEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
			company.setOverview(companyObjectFromCrunch.get(CompanyEnum.OVERVIEW.getLabel().toString()).toString());
			
			String money_raised = companyObjectFromCrunch.get(CompanyEnum.TOTAL_MONEY_RAISED.getLabel().toString()).toString();
			if(money_raised !=null && !money_raised.equals(""))
			{
				money_raised = money_raised.replace("$", "");
				if(money_raised.contains("M"))
				{
					money_raised = money_raised.replace("M", "");
					double amount = Double.valueOf(money_raised);
					amount = amount * 1000000.00;
					company.setTotal_money_raised(amount);
					logger.debug("Amount is in millions");
				}
				else if(money_raised.contains("B"))
				{
					money_raised = money_raised.replace("B", "");
					double amount = Double.valueOf(money_raised);
					amount = amount * 1000000000.00;
					company.setTotal_money_raised(amount);
					logger.debug("Amount is in billions");
				}
			}
			
			setFundingInfo(company, companyObjectFromCrunch);
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object");
		}
	}

	private void setFundingInfo(Company company, JSONObject companyObjectFromCrunch)
	{
		try 
		{
			JSONArray fundingRounds = companyObjectFromCrunch.getJSONArray(CompanyEnum.FUNDING_ROUNDS.getLabel().toString());
			List<Fund> funds = new ArrayList<Fund>();
			
			Map<String, String> allInvestorsFromAllRounds = new HashMap<String, String>();
			for(int index = 0; index < fundingRounds.length() ; index++)
			{
				Fund fund = new Fund(fundingRounds.getJSONObject(index));
				Map<String, String> allInvestors = fund.getUniqueInvestors();
				allInvestorsFromAllRounds.putAll(allInvestors);
				logger.debug("unique investor for this rounds are " + allInvestors.size());
				funds.add(fund);
			}
			logger.debug("unique investor for all rounds are " + allInvestorsFromAllRounds.size());
			
			company.setInvestorPermalinks(allInvestorsFromAllRounds);
			company.setInvestorCount(allInvestorsFromAllRounds.size());
			company.setFund_info(funds);
			
			permalinkMap.putAll(allInvestorsFromAllRounds);
			
		} catch (JSONException e) {
			logger.warn("Failed while getting funding rounds from json object - ");
		}	
	}

	private static void setAngelFieldsFromAngelObject(Company company, JSONObject angelObj) 
	{
		try 
		{
			company.setFoundInAngelList(true);
			
			company.setId(angelObj.getInt(CompanyEnum.ID.getLabel().toString()));
			company.setName(angelObj.get(CompanyEnum.NAME.getLabel().toString()).toString());
			company.setAngellist_url(angelObj.get(CompanyEnum.ANGLELIST_URL.getLabel().toString()).toString());
			if(!angelObj.get(CompanyEnum.LOGO_URL.getLabel().toString()).toString().equals("null"))
			{
				company.setLogo_url(angelObj.get(CompanyEnum.LOGO_URL.getLabel().toString()).toString());
			}
			else
			{
				company.setLogo_url("");
			}
			
			if(!angelObj.get(CompanyEnum.PROD_DESC.getLabel().toString()).toString().equals("null"))
			{
				company.setProduct_desc(angelObj.get(CompanyEnum.PROD_DESC.getLabel().toString()).toString());
			}
			else
			{
				company.setProduct_desc("");
			}
			company.setFollower_count(angelObj.getInt(CompanyEnum.FOLLOWER_COUNT.getLabel().toString()));
			if(!angelObj.get(CompanyEnum.COMPANY_URL.getLabel().toString()).toString().equals("null"))
			{
				company.setCompany_url(angelObj.get(CompanyEnum.COMPANY_URL.getLabel().toString()).toString());
			}
			else
			{
				company.setCompany_url("");
			}
			if(!angelObj.get(CompanyEnum.TWITTER_URL.getLabel().toString()).toString().equals("null"))
			{
				company.setTwitter_url(angelObj.get(CompanyEnum.TWITTER_URL.getLabel().toString()).toString());
			}
			else
			{
				company.setTwitter_url("https://twitter.com/");
			}
			company.setBlog_url(angelObj.get(CompanyEnum.BLOG_URL.getLabel().toString()).toString());
			JSONArray locations = angelObj.getJSONArray(CompanyEnum.LOCATIONS.getLabel().toString());
			for(int index = 0; index < locations.length(); index++)
			{
				Location location = new Location(locations.getJSONObject(index));
				company.addLocation(location);
			}
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object - ");
		}
	}

	private static void setAngelFieldsFromCrunchbase(Company company, JSONObject companyObjectFromCrunch) 
	{
		try 
		{
			company.setId(RandomIdGenerator.getRandomId());
			company.setName(companyObjectFromCrunch.get(CompanyEnum.NAME.getLabel().toString()).toString());
			company.setAngellist_url("");
			company.setLogo_url("");
			company.setProduct_desc("");
			company.setFollower_count(0);
			if(!companyObjectFromCrunch.get(CompanyEnum.HOME_PAGE_URL.getLabel().toString()).toString().equals("null"))
			{
				company.setCompany_url(companyObjectFromCrunch.get(CompanyEnum.HOME_PAGE_URL.getLabel().toString()).toString());
			}
			else
			{
				company.setCompany_url("");
			}
			if(!companyObjectFromCrunch.get(CompanyEnum.TWITTER_USERNAME.getLabel().toString()).toString().equals("null"))
			{
				String twitter_url = "https://twitter.com/"+companyObjectFromCrunch.get(CompanyEnum.TWITTER_USERNAME.getLabel().toString()).toString();
				company.setTwitter_url(twitter_url);
			}
			else
			{
				company.setTwitter_url("https://twitter.com/");
			}
			
			company.setBlog_url(companyObjectFromCrunch.get(CompanyEnum.BLOG_URL.getLabel().toString()).toString());
			JSONArray offices = companyObjectFromCrunch.getJSONArray(CompanyEnum.OFFICES.getLabel().toString());
			for(int index = 0; index < offices.length(); index++)
			{
				Location location = new Location(offices.getJSONObject(index), "city");
				company.addLocation(location);
			}
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object - ");
		}
	}

	private static boolean isCollectionExist(Datastore ds, String permalink) 
	{	
		boolean isCollectionExist = false;
		
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.COMPANY_COLL.getLabel().toString());
		DBObject doc = new BasicDBObject(); 
		doc.put(CompanyEnum._ID.getLabel().toString(), permalink); 
		DBObject dbObject = coll.findOne(doc); 
		if(dbObject != null)
		{
			isCollectionExist = true;
			logger.debug("Company for this permalink already exists in database - " + permalink);
		}
		return isCollectionExist;
	}
}
