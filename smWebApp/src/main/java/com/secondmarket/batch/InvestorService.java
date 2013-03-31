package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Investor;

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
        	
        	Investor investor = new Investor();
        	investor.setId(Integer.valueOf(dbObject.get(InvestorEnum._ID.getLabel()).toString()));
        	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel()).toString());
        	investor.setBio(dbObject.get(InvestorEnum.BIO.getLabel()).toString());
        	investor.setFollower_count(Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString()));
        	investor.setCompany_count(Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString()));
        	//investor.setCompany_id((ArrayList<Integer>)dbObject.get(InvestorEnum.COMPANY_IDS.getLabel()));
        	
        	items.add(investor); // Add to new list
        }
		return items;  // Return list
	}
	
	public Investor get( String id ) 
	{
		logger.debug("Retrieving an existing Investor");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
		DBObject doc = new BasicDBObject(); // Create a new object
		doc.put(InvestorEnum._ID.getLabel().toString(), id); // Put id to search
        DBObject dbObject = coll.findOne(doc);    // Find and return the investor with the given id

        Investor investor = new Investor();  // Map DBOject to investor
        investor.setId(Integer.valueOf(dbObject.get(InvestorEnum._ID.getLabel()).toString()));
    	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel()).toString());
    	investor.setBio(dbObject.get(InvestorEnum.BIO.getLabel()).toString());
    	investor.setFollower_count(Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString()));
    	investor.setCompany_count(Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString()));
    	//investor.setCompany_id((ArrayList<Integer>)dbObject.get(InvestorEnum.COMPANY_IDS.getLabel()));
        
		return investor;// Return investor
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