package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.secondmarket.common.MapUtil;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;
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
	
	public List<Company> companyRankingByFundTime(String periodPast)
	{
		logger.debug("Retrieving companies ranking by fund time");
		
		//Bring user's choice in as a parameter
		int period=0;
		switch (Integer.valueOf(periodPast)){
		case 1: 
			period = 3;	break;
		case 2:
			period = 6;	break;
		case 3:
			period = 12; break;
		case 4:
			period = 24; break;
		default:
			period = 36;
		}
		
		//Get the start date of funding
		Date current = new Date();  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(current);  
		cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)-period));  
		current = cal.getTime();  
		
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(), 
				CommonStrings.COMPANY_COLL.getLabel().toString());
		DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); 
		
		//a hashmap for company and fund_p for sorting
		HashMap<String, Double> CompanyFund = new HashMap<String, Double>();
		HashMap<String, Company> companyPermalink = new HashMap<String, Company>();

		while (cur.hasNext()) 
		{
			DBObject dbObject = cur.next(); 
			Company company = getCompanyObject(dbObject); // bug
			companyPermalink.put(company.getPermalink(), company);
			double fund_p = 0.0;
			
			List<Fund> all_fund = company.getFund_info(); 
			for (Fund each_fund: all_fund)
			{
				@SuppressWarnings("deprecation")
				Date f_date = new Date(each_fund.getFunded_year().intValue()-1900, each_fund.getFunded_month().intValue(), each_fund.getFunded_day().intValue());
				//if in period, add to fund_p
				if(f_date.after(current))
				{
					fund_p += each_fund.getRaised_amount();					
				}
			}
			//insert fund_p and company pair to hashmap
			CompanyFund.put(company.getPermalink(), fund_p);
		}
		//items= sorted company based on fund_p
		HashMap<String, Double> sortedMap = MapUtil.sortHashMapOfString(CompanyFund);
		for(Entry<String, Double> entry : sortedMap.entrySet())
		{
			items.add(companyPermalink.get(entry.getKey()));
		}
		return items;
	}

	public List<Company> filterByFunds(String[] fundRange) 
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
			 
			for (String each : fundRange) {
				if (each.equals("1")) {
					if ((company.getTotal_money_raised()*1000000.0) < 50000)
						items.add(company);
				} else if (each.equals("2")) {
					if ((company.getTotal_money_raised()*1000000.0) < 1000000
							&& (company.getTotal_money_raised()*1000000.0) >= 50000)
						items.add(company);
				} else if (each.equals("3")) {
					if ((company.getTotal_money_raised()*1000000.0) < 3000000
							&& (company.getTotal_money_raised()*1000000.0) >= 1000000)
						items.add(company);
				} else if (each.equals("4")) {
					if ((company.getTotal_money_raised()*1000000.0) < 5000000
							&& (company.getTotal_money_raised()*1000000.0) >= 3000000)
						items.add(company);
				} else if (each.equals("5")) {
					if ((company.getTotal_money_raised()*1000000.0) >= 5000000)
						items.add(company);
				}
			}
		}
		return items; 
	}

	public List<Company> filterByLocation(String[] loc) 
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
			
			List<Location> all_locations = company.getLocations();
			done:for(Location each_location: all_locations)
			{
				String location_name = each_location.getName();
				for (String each : loc) {
					if (each.equals("1")){
						if (location_name.equalsIgnoreCase("san francisco")){
							items.add(company);
							break done;
						}
					} else if (each.equals("2")){
						if (location_name.equalsIgnoreCase("new york, ny")){
							items.add(company);
							break done;
						}
					} else if (each.equals("3")){
						if (location_name.equalsIgnoreCase("san jose")){
							items.add(company);
							break done;
						}
					} else if (each.equals("4")){
						if (!location_name.equalsIgnoreCase("san francisco") &&
								!location_name.equalsIgnoreCase("new york, ny") &&
								!location_name.equalsIgnoreCase("san jose"))
							items.add(company);
						break done;
					}
				}
			}
		}
		return items; 
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
			company.setTwitter_url(dbObject.get(CompanyEnum.TWITTER_URL.getLabel().toString()).toString());
			company.setBlog_url(dbObject.get(CompanyEnum.BLOG_URL.getLabel().toString()).toString());
			company.setCrunchbase_url(dbObject.get(CompanyEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
			
			String overview = dbObject.get(CompanyEnum.OVERVIEW.getLabel().toString()).toString();
			overview = overview.replaceAll("\\<.*?>","");
			company.setOverview(overview);
			
			double total_funds = Double.valueOf(dbObject.get(CompanyEnum.TOTAL_MONEY_RAISED.getLabel().toString()).toString());
			double total_funds_in_millions = (total_funds / 1000000.0);
			company.setTotal_money_raised(Double.valueOf(Double.valueOf(String.format("%.2f", total_funds_in_millions))));
			company.setInvestorPermalinks((Map<String, String>) dbObject.get(CompanyEnum.ALL_INVESTOR.getLabel().toString()));
			company.setInvestorCount(Integer.valueOf(dbObject.get(CompanyEnum.INVESTOR_COUNT.getLabel().toString()).toString()));
			
			List<BasicDBObject> fundObjects = (List<BasicDBObject>) dbObject.get(CompanyEnum.FUND_INFO.getLabel().toString());
			List<Fund> funds = new ArrayList<Fund>();
			if (fundObjects != null) 
			{
				for (BasicDBObject fund : fundObjects) 
				{
					Fund fundInfo = new Fund(fund);
					funds.add(fundInfo);
				}
			}
			company.setFund_info(funds);
			
			double calculatedTotalAmount = 0.0;
			for(Fund fund : company.getFund_info())
			{
				calculatedTotalAmount += fund.getRaised_amount();
			}
			company.setTotal_money_raised((calculatedTotalAmount/1000000.0));
			
			List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel().toString());
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
			company.setLocations(locations);
		}
		return company;
	}
}
