package com.secondmarket.core;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.MongoDBFactory;

public class Normalization 
{	
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static void main(String args[]) throws UnknownHostException{
		PropertyConfigurator.configure("log4j.properties");
		
		DBCollection people = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
		DBCollection company = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString()); // Retrieve collection
		
		double investor_highest_follower_count = maxFcPeople(people);
		double investor_highest_company_count = maxCompInv(people);
		double company_highest_follower_count = maxFcCompany(company);
		
		DBCursor people_cur = people.find();
		while(people_cur.hasNext())
		{
	        DBObject dbObject = people_cur.next();// Map DBOject to investor
	        normalizePeopleData(dbObject, investor_highest_follower_count, investor_highest_company_count);
	        people.save(dbObject);
		}
		
		DBCursor company_cur = company.find();
		while(company_cur.hasNext())
		{
	        DBObject dbObject = company_cur.next();// Map DBOject to company
	        normalizeCompanyData(dbObject, company_highest_follower_count);
	        company.save(dbObject);
		}
		logger.debug("Normalization completed and persisted");
	}

	protected static double maxFcPeople(DBCollection people)
	{
		DBCursor cur = people.find().sort( new BasicDBObject( InvestorEnum.FOLLOWER_COUNT.getLabel().toString() , -1 ));
		DBObject dbObject = cur.next();
		double highest_follower_count = (double)Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString());
		return highest_follower_count;
	}

	protected static double maxCompInv(DBCollection people)
	{
		DBCursor cur = people.find().sort( new BasicDBObject( InvestorEnum.COMPANY_COUNT.getLabel().toString() , -1 ));
		DBObject dbObject = cur.next();
		double highest_company_count = (double)Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString());
		return highest_company_count;
	}
	
	protected static double maxFcCompany(DBCollection company)
	{
		DBCursor cur = company.find().sort( new BasicDBObject( CompanyEnum.FOLLOWER_COUNT.getLabel().toString() , -1 ));
		DBObject dbObject = cur.next();
		double highest_follower_count = (double)Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString());
		return highest_follower_count;
	}
	
	protected static void normalizePeopleData(DBObject dbObject, double highest_follower_count, double highest_company_count){
		double follower_count = 0.0;
		double company_count = 0.0;
		if(!dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString().equalsIgnoreCase(""))
		{
			follower_count = (double)Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString());
		}
		if(!dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString().equalsIgnoreCase(""))
		{
			company_count = (double)Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString());
		}
		
		double follower_count_norm = follower_count/highest_follower_count;
		double company_count_norm = company_count/highest_company_count;
		
		logger.debug(String.format("%.4f", follower_count_norm));
		logger.debug(String.format("%.4f", company_count_norm));
		
		dbObject.put(InvestorEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString(), Double.valueOf(String.format("%.4f", follower_count_norm)));
		dbObject.put(InvestorEnum.NORMALIZED_COMAPNY_SCORE.getLabel().toString(), Double.valueOf(String.format("%.4f", company_count_norm)));
	}
	
	protected static void normalizeCompanyData(DBObject dbObject, double highest_follower_count){
		double follower_count = 0.0;
		if(!dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString().equalsIgnoreCase(""))
		{
			follower_count = (double)Integer.valueOf(dbObject.get(CompanyEnum.FOLLOWER_COUNT.getLabel()).toString());
		}
		
		double follower_count_norm = follower_count/highest_follower_count;
		
		logger.debug(String.format("%.4f", follower_count_norm));
		dbObject.put(CompanyEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString(), Double.valueOf(String.format("%.4f", follower_count_norm)));
	}
}
