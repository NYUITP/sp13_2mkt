package com.secondmarket.core;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.InvestorEnum;

public class Normalization 
{	
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static void main(String args[]) throws UnknownHostException{
		DBCollection people = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
		
		double highest_follower_count = maxFcPeople(people);
		double highest_company_count = maxCompInv(people);
		
		DBCursor cur = people.find();
		while(cur.hasNext())
		{
	        DBObject dbObject = cur.next();// Map DBOject to investor
	        normalize(dbObject, highest_follower_count, highest_company_count);
	        people.save(dbObject);
		}
		logger.debug("Normalization completed and persisted");
	}

	protected static double maxFcPeople(DBCollection people)
	{
		DBCursor cur = people.find().sort( new BasicDBObject( "follower_count" , -1 ));
		DBObject dbObject = cur.next();
		double highest_follower_count = (double)Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel()).toString());
		return highest_follower_count;
	}

	protected static double maxCompInv(DBCollection people)
	{
		DBCursor cur = people.find().sort( new BasicDBObject( "company_count" , -1 ));
		DBObject dbObject = cur.next();
		double highest_company_count = (double)Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel()).toString());
		return highest_company_count;
	}
	
	protected static void normalize(DBObject dbObject, double highest_follower_count, double highest_company_count){
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
}
