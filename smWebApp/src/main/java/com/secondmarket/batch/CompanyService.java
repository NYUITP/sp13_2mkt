package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Location;

@Service("companyService")
@Transactional
public class CompanyService 
{
	protected static Logger logger = Logger.getLogger("batch");
	public CompanyService() {}

	@SuppressWarnings("unchecked")
	public List<Company> getAll() 
	{
		logger.debug("Retrieving all companies");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());
    	DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); // Create new list
		
		// Iterate cursor
        while(cur.hasNext())
        {
        	DBObject dbObject = cur.next(); // Map DBOject to company
        	Company company = new Company();
        	company.setId(Integer.valueOf(dbObject.get(CompanyEnum._ID.getLabel()).toString()));
        	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
        	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
        	double total_funds_in_thousands = Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString());
        	double total_funds_in_millions = (total_funds_in_thousands/1000.0);
        	company.setTotal_funding(Double.valueOf(Double.valueOf(String.format("%.2f", total_funds_in_millions))));
        	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
        	company.setLogo_url(dbObject.get(CompanyEnum.LOGO_URL.getLabel()).toString());
        	company.setCompany_url(dbObject.get(CompanyEnum.COMPANY_URL.getLabel()).toString());
        	company.setTwitter_url(dbObject.get(CompanyEnum.TWITTER_URL.getLabel()).toString());
        	company.setBlog_url(dbObject.get(CompanyEnum.BLOG_URL.getLabel()).toString());
        	company.setHigh_concept(dbObject.get(CompanyEnum.HIGH_CONCEPT.getLabel()).toString());
        	company.setAngellist_url(dbObject.get(CompanyEnum.ANGLELIST_URL.getLabel()).toString());
        	List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel());
        	List<Location> locations = new ArrayList<Location>();
        	if(locationObjects !=null)
        	{
	        	for(BasicDBObject location : locationObjects)
	        	{
	        		try {
						JSONObject locObj = new JSONObject(location.toString());
						Location loc = new Location(locObj);
						locations.add(loc);
					} catch (JSONException e)
					{
						e.printStackTrace();
					}
	        	}
        	}
        	company.setLocations(locations);
        	items.add(company); // Add to new list
        }
		return items;  // Return list
	}
	
	@SuppressWarnings("unchecked")
	public Company get( String id ) 
	{
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve collection
		DBObject doc = new BasicDBObject(); // Create a new object
        doc.put(CompanyEnum._ID.getLabel().toString(), id); // Put id to search
        DBObject dbObject = coll.findOne(doc);  // Find and return the Company with the given id
        
        Company company = new Company(); // Map DBOject to Company
        company.setId(Integer.valueOf(dbObject.get(CompanyEnum._ID.getLabel()).toString()));
    	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
    	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
    	double total_funds_in_thousands = Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString());
    	double total_funds_in_millions = (total_funds_in_thousands/1000.0);
    	company.setTotal_funding(Double.valueOf(Double.valueOf(String.format("%.2f", total_funds_in_millions))));
    	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
    	company.setLogo_url(dbObject.get(CompanyEnum.LOGO_URL.getLabel()).toString());
    	company.setCompany_url(dbObject.get(CompanyEnum.COMPANY_URL.getLabel()).toString());
    	company.setTwitter_url(dbObject.get(CompanyEnum.TWITTER_URL.getLabel()).toString());
    	company.setBlog_url(dbObject.get(CompanyEnum.BLOG_URL.getLabel()).toString());
    	company.setHigh_concept(dbObject.get(CompanyEnum.HIGH_CONCEPT.getLabel()).toString());
    	company.setAngellist_url(dbObject.get(CompanyEnum.ANGLELIST_URL.getLabel()).toString());
    	List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel());
    	List<Location> locations = new ArrayList<Location>();
    	if(locationObjects !=null)
    	{
        	for(BasicDBObject location : locationObjects)
        	{
        		try {
					JSONObject locObj = new JSONObject(location.toString());
					Location loc = new Location(locObj);
					locations.add(loc);
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
        	}
    	}
    	company.setLocations(locations);
        
		return company; // Return company
	}
	
public List<Company> filterByFunds(String[] fundRange){
		
		logger.debug("Retrieving all companies");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());
    	DBCursor cur = coll.find();// Retrieve cursor for iterating records
		List<Company> items = new ArrayList<Company>(); // Create new list
		
		// Iterate cursor
        while(cur.hasNext())
        {
        	DBObject dbObject = cur.next(); // Map DBOject to company
        	Company company = new Company();
        	company.setId(Integer.valueOf(dbObject.get(CompanyEnum._ID.getLabel()).toString()));
        	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
        	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
        	company.setTotal_funding(Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString()));
        	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
        	company.setLogo_url(dbObject.get(CompanyEnum.LOGO_URL.getLabel()).toString());
        	company.setCompany_url(dbObject.get(CompanyEnum.COMPANY_URL.getLabel()).toString());
        	company.setTwitter_url(dbObject.get(CompanyEnum.TWITTER_URL.getLabel()).toString());
        	company.setBlog_url(dbObject.get(CompanyEnum.BLOG_URL.getLabel()).toString());
        	/**
        	 * filter function
        	 */
   
        	for(String each:fundRange){
        		
        		if(each.equals("1")){
        			if(company.getTotal_funding()<500000)
        				items.add(company); 
        		}
        		else if(each.equals("2")){
        			if(company.getTotal_funding()<1000000 && company.getTotal_funding()>=500000)
        				items.add(company); 
        		}
        		else if(each.equals("3")){
        			if(company.getTotal_funding()<5000000 && company.getTotal_funding()>=1000000)
        				items.add(company); 
        		}
        		else if(each.equals("4")){
        			if(company.getTotal_funding()<100000000 && company.getTotal_funding()>=5000000)
        				items.add(company); 
        		}
        		else if(each.equals("5")){
        			if(company.getTotal_funding()>=100000000)
        				items.add(company); 
        		}
        	}
        }
		return items;  // Return list

	}
}
