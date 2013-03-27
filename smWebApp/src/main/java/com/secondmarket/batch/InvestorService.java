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
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.InvestorEnum;
import com.secondmarket.service.MongoDBFactory;

@Service("investorService")
@Transactional
public class InvestorService {

	protected static Logger logger = Logger.getLogger("batch");
	
	public InvestorService() {}
	
	/**
	 * Retrieves all investors
	 */
	@SuppressWarnings("unchecked")
	public List<Investor> getAll() {
		logger.debug("Retrieving all investors");
		
		// Retrieve collection
		DBCollection coll = MongoDBFactory.getCollection("secondmarket","People");
		// Retrieve cursor for iterating records
    	DBCursor cur = coll.find();
    	// Create new list
		List<Investor> items = new ArrayList<Investor>();
		// Iterate cursor
        while(cur.hasNext()) {
        	// Map DBOject to investor
        	DBObject dbObject = cur.next();
        	Investor investor = new Investor();
        	
        	investor.setId(Integer.valueOf(dbObject.get(InvestorEnum.ID.getLabel()).toString()));
        	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel()).toString());
        	investor.setBio(dbObject.get(InvestorEnum.BIO.getLabel()).toString());
        	investor.setFollower_count(Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString()));
        	investor.setCompany_count(Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString()));
        	//investor.setCompany_id((ArrayList<Integer>)dbObject.get(InvestorEnum.COMPANY_IDS.getLabel()));

        	// Add to new list
        	items.add(investor);
        }
        
        // Return list
		return items;
	}
	
	/**
	 * Retrieves a single investor
	 */
	@SuppressWarnings("unchecked")
	public Investor get( String id ) {
		logger.debug("Retrieving an existing Investor");
		
		// Retrieve collection
		DBCollection coll = MongoDBFactory.getCollection("secondmarket","People");
		// Create a new object
		DBObject doc = new BasicDBObject();
		// Put id to search
        doc.put("id", id);
        
        // Find and return the investor with the given id
        DBObject dbObject = coll.findOne(doc);
        
        // Map DBOject to investor
        Investor investor = new Investor();
        investor.setId(Integer.valueOf(dbObject.get(InvestorEnum.ID.getLabel()).toString()));
    	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel()).toString());
    	investor.setBio(dbObject.get(InvestorEnum.BIO.getLabel()).toString());
    	investor.setFollower_count(Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString()));
    	investor.setCompany_count(Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString()));
    	//investor.setCompany_id((ArrayList<Integer>)dbObject.get(InvestorEnum.COMPANY_IDS.getLabel()));
    	
        // Return investor
		return investor;
	}

	/**
	 * Adds a new investor
	 */
	public Boolean add(Investor investor) {
		logger.debug("Adding a new investor");
		
		try {
			// Retrieve collection
			DBCollection coll = MongoDBFactory.getCollection("secondmarket","People");
			// Create a new object
			BasicDBObject doc = new BasicDBObject();
			
	        doc.put(InvestorEnum.ID.getLabel(), investor.getId()); 
	        doc.put(InvestorEnum.NAME.getLabel(), investor.getName());
	        doc.put(InvestorEnum.BIO.getLabel(), investor.getBio());
	        doc.put(InvestorEnum.FOLLOWER_COUNT.getLabel(), investor.getFollower_count());
	        doc.put(InvestorEnum.COMPANY_COUNT.getLabel(), investor.getCompany_count());
	        doc.put(InvestorEnum.COMPANY_IDS.getLabel(), investor.getCompany_id());
	        // Save new investor
	        coll.insert(doc);
	        
			return true;
			
		} catch (Exception e) {
			logger.error("An error has occurred while trying to add new investor", e);
			return false;
		}
	}
}