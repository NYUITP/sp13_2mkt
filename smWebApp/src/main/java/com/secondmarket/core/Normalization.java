package com.secondmarket.core;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.Financial_OrgEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.MongoDBFactory;

public class Normalization 
{	
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static void cacluclateNormalizedScoreForData()
	{
		DBCollection people = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.PEOPLE_COLL.getLabel().toString()); 
		DBCollection company = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.COMPANY_COLL.getLabel().toString()); 
		DBCollection finOrg = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.FINANCIAL_ORG.getLabel().toString()); 
		
		double investor_highest_follower_count = getMaxValueOfGivenFieldInGivenCollection(people, InvestorEnum.FOLLOWER_COUNT.getLabel().toString());
		double investor_highest_company_count = getMaxValueOfGivenFieldInGivenCollection(people, InvestorEnum.COMPANY_COUNT.getLabel().toString());
		double company_highest_follower_count = getMaxValueOfGivenFieldInGivenCollection(company, CompanyEnum.FOLLOWER_COUNT.getLabel().toString());
		double finOrg_highest_follower_count = getMaxValueOfGivenFieldInGivenCollection(finOrg, Financial_OrgEnum.FOLLOWER_COUNT.getLabel().toString());
		double finOrg_highest_company_count = getMaxValueOfGivenFieldInGivenCollection(finOrg, Financial_OrgEnum.COMPANY_COUNT.getLabel().toString());
		
		DBCursor people_cur = people.find();
		while(people_cur.hasNext())
		{
	        DBObject dbObject = people_cur.next();
	        normalizeField(dbObject, investor_highest_follower_count, InvestorEnum.FOLLOWER_COUNT.getLabel().toString(), 
	        		InvestorEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString());
	        normalizeField(dbObject, investor_highest_company_count, InvestorEnum.COMPANY_COUNT.getLabel().toString(), 
	        		InvestorEnum.NORMALIZED_COMPANY_SCORE.getLabel().toString());
	        people.save(dbObject);
		}
		
		DBCursor company_cur = company.find();
		while(company_cur.hasNext())
		{
	        DBObject dbObject = company_cur.next();
	        normalizeField(dbObject, company_highest_follower_count, CompanyEnum.FOLLOWER_COUNT.getLabel().toString(), 
	        		CompanyEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString());
	        company.save(dbObject);
		}
		
		DBCursor finOrg_cur = finOrg.find();
		while(finOrg_cur.hasNext())
		{
	        DBObject dbObject = finOrg_cur.next();
	        normalizeField(dbObject, finOrg_highest_follower_count, Financial_OrgEnum.FOLLOWER_COUNT.getLabel().toString(), 
	        		Financial_OrgEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString());
	        normalizeField(dbObject, finOrg_highest_company_count, Financial_OrgEnum.COMPANY_COUNT.getLabel().toString(), 
	        		Financial_OrgEnum.NORMALIZED_COMPANY_SCORE.getLabel().toString());
	        finOrg.save(dbObject);
		}
	}

	protected static double getMaxValueOfGivenFieldInGivenCollection(DBCollection collection, String field)
	{
		DBCursor cur = collection.find().sort( new BasicDBObject( field , -1 ));
		DBObject dbObject = cur.next();
		double highest_count = (double)Integer.valueOf(dbObject.get(field).toString());
		return highest_count;
	}
	
	protected static void normalizeField(DBObject dbObject, double highest_count, String field, String fieldToUpdate)
	{
		double count_from_collection = 0.0;
		if(!dbObject.get(field).toString().equalsIgnoreCase(""))
		{
			count_from_collection = (double)Integer.valueOf(dbObject.get(field).toString());
		}
		
		double count_norm = (count_from_collection/highest_count);
		
		logger.debug(String.format("%.4f", count_norm));
		dbObject.put(fieldToUpdate, Double.valueOf(String.format("%.4f", count_norm)));
	}
}
