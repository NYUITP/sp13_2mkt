package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.secondmarket.common.FundEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MapUtil;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Location;

@Service("companyService")
@Transactional
public class CompanyService {
	protected static Logger logger = Logger.getLogger("batch");

	public CompanyService() {}

	public List<Company> getAll() 
	{
		logger.debug("Retrieving all companies");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());
		DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); // Create new list

		// Iterate cursor
		while (cur.hasNext()) {
			DBObject dbObject = cur.next(); // Map DBOject to company
			Company company = getCompanyObject(dbObject);
			items.add(company); // Add to new list
		}
		return items; // Return list
	}

	public List<Company> get(List<Integer> ids) 
	{
		logger.debug("Retrieving all companies for investor");
		List<Company> items = new ArrayList<Company>(); // Create new list
		BasicDBList docIds = new BasicDBList();
		docIds.addAll(ids);
		
		DBObject inClause = new BasicDBObject("$in", docIds);
        DBObject query = new BasicDBObject(CompanyEnum._ID.getLabel().toString(), inClause);
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve
		DBCursor dbCursor = coll.find(query);
        if (dbCursor != null)
        {
            while (dbCursor.hasNext())
            {
            	DBObject dbObject = dbCursor.next(); // Map DBOject to company
    			Company company = getCompanyObject(dbObject);
    			items.add(company); // Add to new list
            }
        }
		return items; // Return company
	}
	
	public Company get(Integer id) 
	{
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve
		DBObject doc = new BasicDBObject(); // Create a new object
		doc.put(CompanyEnum._ID.getLabel().toString(), id); // Put id to search
		DBObject dbObject = coll.findOne(doc); // Find and return the Company
		Company company = getCompanyObject(dbObject);	
		return company; // Return company
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
		
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(), CommonStrings.COMPANY_COLL.getLabel().toString());
		DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); // Create new list
		
		//a hashmap for company and fund_p for sorting
		HashMap<Integer, Double> CompanyFund = new HashMap<Integer, Double>();
		HashMap<Integer, Company> companyId = new HashMap<Integer, Company>();
		
		while (cur.hasNext()) {
			DBObject dbObject = cur.next(); // Map DBOject to company
			Company company = getCompanyObject(dbObject); // bug
			companyId.put(company.getId(), company);
			double fund_p = 0.0;
			
			List<Fund> all_fund = company.getFund_info(); 
			for (Fund each_fund: all_fund){
				@SuppressWarnings("deprecation")
				Date f_date = new Date(each_fund.getFunded_year().intValue()-1900, each_fund.getFunded_month().intValue(), each_fund.getFunded_day().intValue());
	
				//if in period, add to fund_p
				if(f_date.after(current)){
					fund_p += each_fund.getRaised_amount();					
				}
			}
			//insert fund_p and company pair to hashmap
			//System.out.println(company.getName()+": total funding - "+company.getTotal_funding()+", in funding period - "+fund_p);
			CompanyFund.put(company.getId(), fund_p);
		}
		
		//items= sorted company based on fund_p
		HashMap<Integer, Double> sortedMap = MapUtil.sortHashMap(CompanyFund);
		
		for(Entry<Integer, Double> entry : sortedMap.entrySet()){
			items.add(companyId.get(entry.getKey()));
		}

		return items;
	}

	public List<Company> filterByFunds(String[] fundRange) 
	{
		logger.debug("Retrieving all companies");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(), CommonStrings.COMPANY_COLL.getLabel().toString());
		DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); // Create new list

		// Iterate cursor
		while (cur.hasNext()) {
			DBObject dbObject = cur.next(); // Map DBOject to company
			Company company = getCompanyObject(dbObject);	
			 
			//filter function
			for (String each : fundRange) {
				if (each.equals("1")) {
					if (company.getTotal_funding() < 1000000)
						items.add(company);
				} else if (each.equals("2")) {
					if (company.getTotal_funding() < 5000000
							&& company.getTotal_funding() >= 1000000)
						items.add(company);
				} else if (each.equals("3")) {
					if (company.getTotal_funding() < 100000000
							&& company.getTotal_funding() >= 5000000)
						items.add(company);
				} else if (each.equals("4")) {
					if (company.getTotal_funding() < 500000000
							&& company.getTotal_funding() >= 100000000)
						items.add(company);
				} else if (each.equals("5")) {
					if (company.getTotal_funding() >= 500000000)
						items.add(company);
				}
			}
		}
		return items; // Return list
	}

	public List<Company> filterByLocation(String[] loc) 
	{
		logger.debug("Retrieving all companies");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(), CommonStrings.COMPANY_COLL.getLabel().toString());
		DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); // Create new list

		// Iterate cursor
		while (cur.hasNext()) {
			DBObject dbObject = cur.next(); // Map DBOject to company
			Company company = getCompanyObject(dbObject);	
			
			//location function
			List<Location> all_locations = company.getLocations();
			done:for(Location each_location: all_locations){
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
		return items; // Return list
	}
	
	@SuppressWarnings("unchecked")
	private Company getCompanyObject(DBObject dbObject) 
	{
		Company company = new Company();
		company.setId(Integer.valueOf(dbObject.get(CompanyEnum._ID.getLabel()).toString()));
		company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
		company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
		double total_funds_in_thousands = Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString());
		double total_funds_in_millions = (total_funds_in_thousands / 1000.0);
		company.setTotal_funding(Double.valueOf(Double.valueOf(String.format("%.2f", total_funds_in_millions))));
		company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
		company.setLogo_url(dbObject.get(CompanyEnum.LOGO_URL.getLabel()).toString());
		company.setCompany_url(dbObject.get(CompanyEnum.COMPANY_URL.getLabel()).toString());
		company.setTwitter_url(dbObject.get(CompanyEnum.TWITTER_URL.getLabel()).toString());
		company.setBlog_url(dbObject.get(CompanyEnum.BLOG_URL.getLabel()).toString());
		company.setHigh_concept(dbObject.get(CompanyEnum.HIGH_CONCEPT.getLabel()).toString());
		company.setAngellist_url(dbObject.get(CompanyEnum.ANGLELIST_URL.getLabel()).toString());
		
		List<BasicDBObject> fundObjects = (List<BasicDBObject>) dbObject.get(FundEnum.FUND_INFO.getLabel());
		List<Fund> fund_info = new ArrayList<Fund>();
		if (fundObjects != null) {
			for (BasicDBObject fund : fundObjects) {
				try {
					JSONObject fObj = new JSONObject(fund.toString());
					Fund fd = new Fund(fObj);
					fund_info.add(fd);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		company.setFund_info(fund_info);

		
		List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel());
		List<Location> locations = new ArrayList<Location>();
		if (locationObjects != null) {
			for (BasicDBObject location : locationObjects) {
				try {
					JSONObject locObj = new JSONObject(location.toString());
					Location loc = new Location(locObj);
					locations.add(loc);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		company.setLocations(locations);
		return company;
	}
}
