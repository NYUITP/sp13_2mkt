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
import com.secondmarket.common.FundEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.common.RandomIdGenerator;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.Location;

public class InvestorDatabaseService 
{
	protected static Logger logger = Logger.getLogger("core");
	
	public static void populateInvestorCollection(Datastore ds, String personPermalink)
	{
		boolean isCollectionExist = isCollectionExist(ds, personPermalink);
		if(!isCollectionExist)
		{
			logger.debug("Working on permalink - " + personPermalink);
			String personDataFromCrunch = AngelCrunchDataService.getObjectFromCrunchbaseUsingPermalink
					(CrunchbaseNamespace.PERSON.getLabel().toString(), personPermalink);
			
			if(!personDataFromCrunch.equals(""))
			{
				JSONObject personObjectFromCrunch = AngelCrunchDataService.parseToJSON(personDataFromCrunch);
				
				if(personObjectFromCrunch != null)
				{
					try 
					{
						String name = personObjectFromCrunch.get(InvestorEnum.FIRST_NAME.getLabel().toString()).toString().trim() + " " +
								personObjectFromCrunch.get(InvestorEnum.LAST_NAME.getLabel().toString()).toString().trim();
						String nameForSearch = name.replace(" ", "+");
						String personDataFromAngel = AngelCrunchDataService.getObjectFromAngelUsingSearchQuery("User", nameForSearch);
						JSONArray personArrayFromAngel = AngelCrunchDataService.parseToJSONArray(personDataFromAngel);
					
						createAndPersistPersonObjects(ds, personObjectFromCrunch, personArrayFromAngel);
					}
					catch (JSONException e) {
						logger.warn("Name field not found in company object" + personPermalink);
					}
				}
			}
		}
	}

	private static void createAndPersistPersonObjects(Datastore ds,
			JSONObject personObjectFromCrunch, JSONArray personArrayFromAngel) throws JSONException 
	{
		Investor investor = new Investor();
		JSONObject jobj = getAngelMatch(personObjectFromCrunch, personArrayFromAngel);
		if(jobj != null)
		{
			try 
			{
				String id = jobj.get(InvestorEnum.ID.getLabel().toString()).toString();
				String angelData = AngelCrunchDataService.getObjectFromAngelUsingId("users", id);
				if(!angelData.equals(""))
				{
					JSONObject angelObj = AngelCrunchDataService.parseToJSON(angelData);
					if(angelObj != null)
					{
						setAngelFieldsFromAngelObject(investor, angelObj);
					}
					else
					{
						setAngelFieldsFromCrunchbase(investor, personObjectFromCrunch);
					}
				}
				else
				{
					setAngelFieldsFromCrunchbase(investor, personObjectFromCrunch);
				}
				
			} catch (JSONException e) {
				
				logger.warn("Couldn't parse array to json object");
			}
		}
		else
		{
			setAngelFieldsFromCrunchbase(investor, personObjectFromCrunch);
		}
		
		setCrunchFieldsFromCrunchbase(investor, personObjectFromCrunch);
		ds.save(investor);
	}


	private static void setCrunchFieldsFromCrunchbase(Investor investor, JSONObject personObjectFromCrunch)
	{
		try 
		{
			investor.setFoundInCrunchbase(true);
			investor.setPermalink(personObjectFromCrunch.get(InvestorEnum.PERMALINK.getLabel().toString()).toString());
			investor.setFirst_name(personObjectFromCrunch.get(InvestorEnum.FIRST_NAME.getLabel().toString()).toString());
			investor.setLast_name(personObjectFromCrunch.get(InvestorEnum.LAST_NAME.getLabel().toString()).toString());
			investor.setOverview(personObjectFromCrunch.get(InvestorEnum.OVERVIEW.getLabel().toString()).toString());
			investor.setCrunchbase_url(personObjectFromCrunch.get(InvestorEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
			
			setInvestmentInfo(investor, personObjectFromCrunch);
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object");
		}
	}

	private static void setInvestmentInfo(Investor investor, JSONObject personObjectFromCrunch) 
	{
		try 
		{
			JSONArray investments = personObjectFromCrunch.getJSONArray(InvestorEnum.INVESTMENTS.getLabel().toString());
			List<Fund> funds = new ArrayList<Fund>();
			Set<String> allCompaniesInvestedIn = new HashSet<String>();
			
			for(int index = 0; index < investments.length() ; index++)
			{
				JSONObject fund_round = investments.getJSONObject(index).getJSONObject(InvestorEnum.FUND_ROUNDS.getLabel().toString());
				
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
			investor.setCompaniesInvestedIn(companiesInvestedIn);
			investor.setCompany_count(allCompaniesInvestedIn.size());
			investor.setFund_info(funds);
			
		} catch (JSONException e) {
			logger.warn("Failed while getting funding rounds from json object - ");
		}
	}

	private static void setAngelFieldsFromCrunchbase(Investor investor, JSONObject personObjectFromCrunch)
	{
		try 
		{
			investor.setId(RandomIdGenerator.getRandomId());
			String name = personObjectFromCrunch.get(InvestorEnum.FIRST_NAME.getLabel().toString()).toString().trim() + " " +
					personObjectFromCrunch.get(InvestorEnum.LAST_NAME.getLabel().toString()).toString().trim();
			investor.setName(name);
			investor.setBio("");
			investor.setFollower_count(0);
			investor.setAngellist_url("");
			investor.setImage("");
			investor.setLinkedin_url("");
			
			if(!personObjectFromCrunch.get(InvestorEnum.TWITTER_USERNAME.getLabel().toString()).toString().equals("null"))
			{
				String twitter_url = "https://twitter.com/"+personObjectFromCrunch.get(InvestorEnum.TWITTER_USERNAME.getLabel().toString()).toString();
				investor.setTwitter_url(twitter_url);
			}
			else
			{
				investor.setTwitter_url("https://twitter.com/");
			}
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object - ");
		}
	}

	private static void setAngelFieldsFromAngelObject(Investor investor, JSONObject angelObj) 
	{
		try 
		{
			investor.setFoundInAngelList(true);
			
			investor.setId(angelObj.getInt(InvestorEnum.ID.getLabel().toString()));
			investor.setName(angelObj.get(InvestorEnum.NAME.getLabel().toString()).toString());
			investor.setBio(angelObj.get(InvestorEnum.BIO.getLabel().toString()).toString());
			investor.setFollower_count(angelObj.getInt(InvestorEnum.FOLLOWER_COUNT.getLabel().toString()));
			investor.setAngellist_url(angelObj.get(InvestorEnum.ANGLELIST_URL.getLabel().toString()).toString());
			investor.setImage(angelObj.get(InvestorEnum.INVESTOR_IMAGE.getLabel().toString()).toString());
			if(!angelObj.get(InvestorEnum.TWITTER_URL.getLabel().toString()).toString().equals("null"))
			{
				investor.setTwitter_url(angelObj.get(InvestorEnum.TWITTER_URL.getLabel().toString()).toString());
			}
			else
			{
				investor.setTwitter_url("https://twitter.com/");
			}
			if(!angelObj.get(InvestorEnum.LINKEDIN_URL.getLabel().toString()).toString().equals("null"))
			{
				investor.setLinkedin_url(angelObj.get(InvestorEnum.LINKEDIN_URL.getLabel().toString()).toString());
			}
			else
			{
				investor.setLinkedin_url("http://www.linkedin.com/");
			}
			JSONArray locations = angelObj.getJSONArray(InvestorEnum.LOCATIONS.getLabel().toString());
			for(int index = 0; index < locations.length(); index++)
			{
				Location location = new Location(locations.getJSONObject(index));
				investor.addLocation(location);
			}
			
		} catch (JSONException e) {
			logger.warn("Failed while getting fields from json object - ");
		}
	}

	private static JSONObject getAngelMatch(JSONObject personObjectFromCrunch, JSONArray personArrayFromAngel) throws JSONException 
	{
		JSONObject jobj = null;
		if(personArrayFromAngel != null && personArrayFromAngel.length() == 1)
		{
			jobj = personArrayFromAngel.getJSONObject(0);
		}
		else if(personArrayFromAngel != null)
		{
			for(int index = 0; index < personArrayFromAngel.length() ; index++)
			{
				JSONObject person = personArrayFromAngel.getJSONObject(index);
				String nameAngel = person.get(InvestorEnum.NAME.getLabel().toString()).toString().trim();
				String nameCrunch = personObjectFromCrunch.get(InvestorEnum.FIRST_NAME.getLabel().toString()).toString().trim()+ " " +
						 personObjectFromCrunch.get(InvestorEnum.LAST_NAME.getLabel().toString()).toString().trim();
				if(nameAngel.equalsIgnoreCase(nameCrunch))
				{
					jobj = person;
					break;
				}
			}
		}
		return jobj;
	}

	private static boolean isCollectionExist(Datastore ds, String permalink) 
	{	
		boolean isCollectionExist = false;
		
		logger.debug("Retrieving an existing investor");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.PEOPLE_COLL.getLabel().toString());
		DBObject doc = new BasicDBObject(); 
		doc.put(InvestorEnum._ID.getLabel().toString(), permalink); 
		DBObject dbObject = coll.findOne(doc); 
		if(dbObject != null)
		{
			isCollectionExist = true;
			logger.debug("Person for this permalink already exists in database - " + permalink);
		}
		return isCollectionExist;
	}
}
