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
import com.secondmarket.domain.Company;
import com.secondmarket.domain.CompanyEnum;
import com.secondmarket.service.MongoDBFactory;


@Service("companyService")
@Transactional
public class CompanyService {

	protected static Logger logger = Logger.getLogger("batch");
	
	public CompanyService() {}
	
	/**
	 * Retrieves all investors
	 */
	public List<Company> getAll() {
		logger.debug("Retrieving all companies");
		
		// Retrieve collection
		DBCollection coll = MongoDBFactory.getCollection("secondmarket","Company");
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
		DBCollection coll = MongoDBFactory.getCollection("secondmarket","Company");
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
}