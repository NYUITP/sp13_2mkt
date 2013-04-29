package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.Location;

@Service("companyService")
@Transactional

public class CompanyService 
{
	protected static Logger logger = Logger.getLogger("batch");

	public CompanyService() {}

	public List<Company> getAll() 
	{
		logger.debug("Retrieving all companies");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.COMPANY_COLL.getLabel().toString());
		DBCursor cur = coll.find();
		List<Company> items = new ArrayList<Company>(); 

		while (cur.hasNext()) 
		{
			DBObject dbObject = cur.next(); 
			Company company = getCompanyObject(dbObject);
			items.add(company); 
		}
		return items; 
	}

	public List<Company> get(List<String> permalinks) 
	{
		logger.debug("Retrieving all companies for given permalink");
		List<Company> items = new ArrayList<Company>(); 
		BasicDBList docPermalinks = new BasicDBList();
		if(permalinks != null)
		{
			docPermalinks.addAll(permalinks);
			
			DBObject inClause = new BasicDBObject("$in", docPermalinks);
	        DBObject query = new BasicDBObject(CompanyEnum._ID.getLabel().toString(), inClause);
			DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
					CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve
			DBCursor dbCursor = coll.find(query);
	        if (dbCursor != null)
	        {
	            while (dbCursor.hasNext())
	            {
	            	DBObject dbObject = dbCursor.next(); 
	    			Company company = getCompanyObject(dbObject);
	    			items.add(company); 
	            }
	        }
		}
		return items; 
	}
	
	public Company get(String permalink) 
	{
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve
		DBObject doc = new BasicDBObject(); 
		doc.put(CompanyEnum._ID.getLabel().toString(), permalink); 
		DBObject dbObject = coll.findOne(doc); 
		Company company = new Company();
		if(dbObject != null)
		{
			company = getCompanyObject(dbObject);	
		}
		return company; 
	}
	
	@SuppressWarnings("unchecked")
	private Company getCompanyObject(DBObject dbObject) 
	{
		Company company = new Company();
		if(dbObject != null)
		{
			company.setPermalink(dbObject.get(CompanyEnum._ID.getLabel().toString()).toString());
			company.setId(Integer.valueOf(dbObject.get(CompanyEnum.ID.getLabel().toString()).toString()));
			company.setName(dbObject.get(CompanyEnum.NAME.getLabel().toString()).toString());
			company.setAngellist_url(dbObject.get(CompanyEnum.ANGLELIST_URL.getLabel().toString()).toString());
			if(!dbObject.get(CompanyEnum.LOGO_URL.getLabel().toString()).toString().equals(""))
			{
				company.setLogo_url(dbObject.get(CompanyEnum.LOGO_URL.getLabel().toString()).toString());
			}
			else
			{
				company.setLogo_url("resources/img/company-logo.png");
			}
			company.setProduct_desc(dbObject.get(CompanyEnum.PROD_DESC.getLabel().toString()).toString());
			company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel().toString()).toString()));
			company.setCompany_url(dbObject.get(CompanyEnum.COMPANY_URL.getLabel().toString()).toString());
			if(!dbObject.get(CompanyEnum.TWITTER_URL.getLabel().toString()).toString().equals(""))
			{
				company.setTwitter_url(dbObject.get(CompanyEnum.TWITTER_URL.getLabel().toString()).toString());
			}
			else
			{
				company.setTwitter_url("https://twitter.com/");
			}
			company.setBlog_url(dbObject.get(CompanyEnum.BLOG_URL.getLabel().toString()).toString());
			company.setCrunchbase_url(dbObject.get(CompanyEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
			
			String overview = dbObject.get(CompanyEnum.OVERVIEW.getLabel().toString()).toString();
			overview = overview.replaceAll("\\<.*?>","");
	    	overview = overview.replace("&#8217;","'");
	    	overview = overview.replaceAll("&#82..;","");
	    	overview = overview.replaceAll("&nbsp;","");
	    	overview = overview.replaceAll("&amp;","&");
			company.setOverview(overview);
			
			double total_funds = Double.valueOf(dbObject.get(CompanyEnum.TOTAL_MONEY_RAISED.getLabel().toString()).toString());
			double total_funds_in_millions = (total_funds / 1000000.0);
			company.setTotal_money_raised(Double.valueOf(Double.valueOf(String.format("%.2f", total_funds_in_millions))));
			company.setInvestorPermalinks((Map<String, String>) dbObject.get(CompanyEnum.ALL_INVESTOR.getLabel().toString()));
			company.setInvestorCount(Integer.valueOf(dbObject.get(CompanyEnum.INVESTOR_COUNT.getLabel().toString()).toString()));
			
			List<BasicDBObject> fundObjects = (List<BasicDBObject>) dbObject.get(CompanyEnum.FUND_INFO.getLabel().toString());
			List<Fund> funds = getFundInfo(fundObjects);
			company.setFund_info(funds);
			
			double calculatedTotalAmount = 0.0;
			for(Fund fund : company.getFund_info())
			{
				calculatedTotalAmount += fund.getRaised_amount();
			}
			double amount = Double.valueOf(String.format("%.2f", (calculatedTotalAmount/1000000.0)));
			company.setTotal_money_raised(amount);
			
			List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel().toString());
			List<Location> locations = getLocationInfo(locationObjects);
			company.setLocations(locations);
		}
		return company;
	}
	
	private List<Location> getLocationInfo(List<BasicDBObject> locationObjects)
	{
		List<Location> locations = new ArrayList<Location>();
		if (locationObjects != null) 
		{
			for (BasicDBObject location : locationObjects) 
			{
				try 
				{
					JSONObject locObj = new JSONObject(location.toString());
					Location loc = new Location(locObj, LocationEnum.LOCATION_NAME.getLabel().toString());
					locations.add(loc);
				} catch (JSONException e){
					logger.warn("Error while building company's location object from database");
				}
			}
		}
		return locations;
	}
	
	private List<Fund> getFundInfo(List<BasicDBObject> fundObjects)
	{
		List<Fund> funds = new ArrayList<Fund>();
		Set<String> allInvestorNames = new HashSet<String>();

		if (fundObjects != null) 
		{
			for (BasicDBObject fund : fundObjects) 
			{
				Fund fundInfo = new Fund(fund);
				List<Investor> inv = fundInfo.getInvestors();
				if(!inv.isEmpty())
				{
					for(Investor in : inv)
					{
						allInvestorNames.add(in.getName());
					}
				}
				
				List<Financial_Org> finOrg = fundInfo.getFinacialOrgs();
				if(!finOrg.isEmpty())
				{
					for(Financial_Org fin : finOrg)
					{
						allInvestorNames.add(fin.getName());
					}
				}
				
				List<Company> comp = fundInfo.getCompanies();
				if(!comp.isEmpty())
				{
					for(Company com : comp)
					{
						allInvestorNames.add(com.getName());
					}
				}
				String allUniqueNames = "";
				for(String name : allInvestorNames)
				{
					allUniqueNames += name +", ";
				}
				fundInfo.setAllInvestorNames(allUniqueNames);
				funds.add(fundInfo);
			}
		}
		return funds;
	}
}
