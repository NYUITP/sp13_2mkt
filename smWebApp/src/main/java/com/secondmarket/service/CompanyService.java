package com.secondmarket.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.dao.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.CompanyEnum;


@Service("companyService")
@Transactional
public class CompanyService {

	protected static Logger logger = Logger.getLogger("service");
	
	public CompanyService() {
		// Initialize our database
		init();
	}
	
	/**
	 * Retrieves all investors
	 */
	public List<Company> getAll() {
		logger.debug("Retrieving all companies");
		
		// Retrieve collection
		DBCollection coll = MongoDBFactory.getCollection("test","companyCollection");
		// Retrieve cursor for iterating records
    	DBCursor cur = coll.find();
    	// Create new list
		List<Company> items = new ArrayList<Company>();
		// Iterate cursor
        while(cur.hasNext()) {
        	// Map DBOject to company
        	DBObject dbObject = cur.next();
        	Company company = new Company();
        	
        	company.setId(Integer.valueOf(dbObject.get(CompanyEnum.ID.getLabel()).toString()));
        	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
        	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
        	company.setTotal_funding(Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString()));
        	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());

        	// Add to new list
        	items.add(company);
        }
        
        // Return list
		return items;
	}
	
	/**
	 * Retrieves a single company
	 */
	public Company get( String id ) {
		logger.debug("Retrieving an existing Company");
		
		// Retrieve collection
		DBCollection coll = MongoDBFactory.getCollection("test","companyCollection");
		// Create a new object
		DBObject doc = new BasicDBObject();
		// Put id to search
        doc.put("id", id);
        
        // Find and return the Company with the given id
        DBObject dbObject = coll.findOne(doc);
        
        // Map DBOject to Company
        Company company = new Company();
        company.setId(Integer.valueOf(dbObject.get(CompanyEnum.ID.getLabel()).toString()));
    	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
    	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
    	company.setTotal_funding(Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString()));
    	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
    	
        // Return company
		return company;
	}
	
	private void init() {
		// Populate our MongoDB database

		logger.debug("Init MongoDB users");
		
		// Drop existing collection
		MongoDBFactory.getCollection("test","companyCollection").drop();
		// Retrieve collection. If not existing, create a new one
		DBCollection coll = MongoDBFactory.getCollection("test","companyCollection");
		
		// Create new object
		BasicDBObject doc = new BasicDBObject();
        doc.put(CompanyEnum.ID.getLabel(), "1");
        doc.put(CompanyEnum.NAME.getLabel(), "AngelList");
        doc.put(CompanyEnum.FOLLOWER_COUNT.getLabel(), "2849");
        doc.put(CompanyEnum.TOTAL_FUNDING.getLabel(), "197");
        doc.put(CompanyEnum.PRODUCT_DESC.getLabel(), "AngelList is a platform for startups to meet investors and talent.");
        coll.insert(doc);
		
        // Create new object
        doc = new BasicDBObject();
        doc.put(CompanyEnum.ID.getLabel(), "2");
        doc.put(CompanyEnum.NAME.getLabel(), "Articulate Labs");
        doc.put(CompanyEnum.FOLLOWER_COUNT.getLabel(), "23");
        doc.put(CompanyEnum.TOTAL_FUNDING.getLabel(), "19");
        doc.put(CompanyEnum.PRODUCT_DESC.getLabel(), "We've developed an intelligent and adaptive joint rehabilitation device");
        coll.insert(doc);
	}
}