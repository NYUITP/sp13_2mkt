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
import com.secondmarket.service.CommonStrings;
import com.secondmarket.service.MongoDBFactory;

@Service("companyService")
@Transactional
public class CompanyService 
{
	protected static Logger logger = Logger.getLogger("batch");
	public CompanyService() {}

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
        	company.setId(Integer.valueOf(dbObject.get(CompanyEnum.ID.getLabel()).toString()));
        	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
        	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
        	company.setTotal_funding(Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString()));
        	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
        	items.add(company); // Add to new list
        }
		return items;  // Return list
	}
	
	public Company get( String id ) 
	{
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve collection
		DBObject doc = new BasicDBObject(); // Create a new object
        doc.put(CompanyEnum.ID.getLabel().toString(), id); // Put id to search
        DBObject dbObject = coll.findOne(doc);  // Find and return the Company with the given id
        
        Company company = new Company(); // Map DBOject to Company
        company.setId(Integer.valueOf(dbObject.get(CompanyEnum.ID.getLabel()).toString()));
    	company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
    	company.setFollower_count(Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString()));
    	company.setTotal_funding(Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString()));
    	company.setProduct_desc(dbObject.get(CompanyEnum.PRODUCT_DESC.getLabel()).toString());
        
		return company; // Return company
	}
}