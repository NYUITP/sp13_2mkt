package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.List;

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
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.Location;

@Service("investorService")
@Transactional
public class InvestorService 
{
	protected static Logger logger = Logger.getLogger("batch");
	public InvestorService() {}

	public List<Investor> getAll() 
	{
		logger.debug("Retrieving all investors");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
    	DBCursor cur = coll.find(); // Retrieve cursor for iterating records
		List<Investor> items = new ArrayList<Investor>(); // Create new list
		
		// Iterate cursor
        while(cur.hasNext()) 
        {
        	DBObject dbObject = cur.next();// Map DBOject to investor
        	Investor investor = getInvestorObject(dbObject);
        	items.add(investor); // Add to new list
        }
		return items;  // Return list
	}
	
	public List<Investor> get(List<Integer> ids) 
	{
		logger.debug("Retrieving all investors for a company");
		List<Investor> items = new ArrayList<Investor>(); // Create new list
		BasicDBList docIds = new BasicDBList();
		if(ids != null)
		{
			docIds.addAll(ids);
			
			DBObject inClause = new BasicDBObject("$in", docIds);
	        DBObject query = new BasicDBObject(InvestorEnum._ID.getLabel().toString(), inClause);
			DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString());// Retrieve
			DBCursor dbCursor = coll.find(query);
	        if (dbCursor != null)
	        {
	            while (dbCursor.hasNext())
	            {
	            	DBObject dbObject = dbCursor.next(); // Map DBOject to investor
	            	Investor investor = getInvestorObject(dbObject);
	    			items.add(investor); // Add to new list
	            }
	        }
		}
		return items; // Return investor
	}
	
	public Investor get( Integer id ) 
	{
		logger.debug("Retrieving an existing Investor");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
		DBObject doc = new BasicDBObject(); // Create a new object
		doc.put(InvestorEnum._ID.getLabel().toString(), id); // Put id to search
        DBObject dbObject = coll.findOne(doc);    // Find and return the investor with the given id
        Investor investor = getInvestorObject(dbObject);
		return investor;// Return investor
	}

	@SuppressWarnings("unchecked")
	private Investor getInvestorObject(DBObject dbObject) 
	{
		Investor investor = new Investor();
    	investor.setId(Integer.valueOf(dbObject.get(InvestorEnum._ID.getLabel()).toString()));
    	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel()).toString());
    	investor.setBio(dbObject.get(InvestorEnum.BIO.getLabel()).toString());
    	investor.setFollower_count(Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString()));
    	investor.setCompany_count(Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString()));
    	investor.setImage(dbObject.get(InvestorEnum.INVESTOR_IMAGE.getLabel()).toString());
    	investor.setFl_norm(Double.valueOf(dbObject.get(InvestorEnum.NORMALIZED_FOLLOWER_SCORE.getLabel()).toString()));
    	investor.setCc_norm(Double.valueOf(dbObject.get(InvestorEnum.NORMALIZED_COMAPNY_SCORE.getLabel()).toString()));
    	investor.setAngellist_url(dbObject.get(InvestorEnum.ANGLELIST_URL.getLabel()).toString());
    	investor.setBlog_url(dbObject.get(InvestorEnum.BLOG_URL.getLabel()).toString());
    	investor.setTwitter_url(dbObject.get(InvestorEnum.TWITTER_URL.getLabel()).toString());
    	investor.setFacebook_url(dbObject.get(InvestorEnum.FB_URL.getLabel()).toString());
    	investor.setLinkedin_url(dbObject.get(InvestorEnum.LINKEDIN_URL.getLabel()).toString());
    	List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel().toString());
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
    	investor.setLocations(locations);
    	investor.setCompany_id((ArrayList<Integer>)dbObject.get(InvestorEnum.COMPANY_IDS.getLabel().toString()));	
		return investor;
	}
	public Boolean add(Investor investor) 
	{
		logger.debug("Adding a new investor");	
		try 
		{			
			DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
			BasicDBObject doc = new BasicDBObject(); // Create a new object
	        doc.put(InvestorEnum._ID.getLabel(), investor.getId()); 
	        doc.put(InvestorEnum.NAME.getLabel(), investor.getName());
	        doc.put(InvestorEnum.BIO.getLabel(), investor.getBio());
	        doc.put(InvestorEnum.FOLLOWER_COUNT.getLabel(), investor.getFollower_count());
	        doc.put(InvestorEnum.COMPANY_COUNT.getLabel(), investor.getCompany_count());
	        doc.put(InvestorEnum.COMPANY_IDS.getLabel(), investor.getCompany_id());
	        coll.insert(doc); // Save new investor
			return true;
			
		} catch (Exception e) {
			logger.error("An error has occurred while trying to add new investor", e);
			return false;
		}
	}
}