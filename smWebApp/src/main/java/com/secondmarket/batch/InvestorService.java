package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.Collections;
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
import com.secondmarket.common.FundComparator;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.Location;

@Service("investorService")
@Transactional

public class InvestorService 
{
	protected static Logger logger = Logger.getLogger("batch");
	private static List<Investor> allInvestorsInDatabase = new ArrayList<Investor>(); 
	
	public InvestorService() {}

	public List<Investor> getAllInvestors() 
	{
		logger.debug("Retrieving all investors");
		if(allInvestorsInDatabase.isEmpty())
		{
			DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
					CommonStrings.PEOPLE_COLL.getLabel().toString()); 
	    	DBCursor cur = coll.find(); 
			
	        while(cur.hasNext()) 
	        {
	        	DBObject dbObject = cur.next();
	        	Investor investor = getInvestorObject(dbObject);
	        	allInvestorsInDatabase.add(investor); 
	        }
		}
		return allInvestorsInDatabase;  
	}
	
	public List<Investor> getInvestorsGivenPermalinks(List<String> permalinks) 
	{
		logger.debug("Retrieving all investors for given permalink");
		List<Investor> items = new ArrayList<Investor>(); // Create new list
		BasicDBList docIds = new BasicDBList();
		if(permalinks != null)
		{
			docIds.addAll(permalinks);
			
			DBObject inClause = new BasicDBObject("$in", docIds);
	        DBObject query = new BasicDBObject(InvestorEnum._ID.getLabel().toString(), inClause);
			DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
					CommonStrings.PEOPLE_COLL.getLabel().toString());
			DBCursor dbCursor = coll.find(query);
	        if (dbCursor != null)
	        {
	            while (dbCursor.hasNext())
	            {
	            	DBObject dbObject = dbCursor.next(); 
	            	Investor investor = getInvestorObject(dbObject);
	    			items.add(investor); 
	            }
	        }
		}
		return items; 
	}
	
	public Investor getInvestor( String permalink ) 
	{
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.PEOPLE_COLL.getLabel().toString()); 
		DBObject doc = new BasicDBObject(); 
		doc.put(InvestorEnum._ID.getLabel().toString(), permalink); 
        DBObject dbObject = coll.findOne(doc);
        Investor investor = new Investor();
        if(dbObject != null)
        {
        	investor = getInvestorObject(dbObject);
        }
		return investor;
	}
	
	public DBObject getdbObject( String permalink ) 
	{
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.PEOPLE_COLL.getLabel().toString()); 
		DBObject doc = new BasicDBObject(); 
		doc.put(InvestorEnum._ID.getLabel().toString(), permalink); 
        DBObject dbObject = coll.findOne(doc);
        return dbObject;
	}

	@SuppressWarnings("unchecked")
	private Investor getInvestorObject(DBObject dbObject) 
	{
		Investor investor = new Investor();
		if(dbObject != null )
		{
			investor.setPermalink(dbObject.get(InvestorEnum._ID.getLabel().toString()).toString());
	    	investor.setId(Integer.valueOf(dbObject.get(InvestorEnum.ID.getLabel()).toString()));
	    	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel().toString()).toString());
	    	investor.setBio(dbObject.get(InvestorEnum.BIO.getLabel().toString()).toString());
	    	investor.setFollower_count(Integer.valueOf(dbObject.get(InvestorEnum.FOLLOWER_COUNT.getLabel().toString()).toString()));
	    	investor.setCompany_count(Integer.valueOf(dbObject.get(InvestorEnum.COMPANY_COUNT.getLabel().toString()).toString()));
	    	if(!dbObject.get(InvestorEnum.INVESTOR_IMAGE.getLabel().toString()).toString().equals(""))
	    	{
	    		investor.setImage(dbObject.get(InvestorEnum.INVESTOR_IMAGE.getLabel().toString()).toString());
	    	}
	    	else
	    	{
	    		investor.setImage("resources/img/user-icon.png");
	    	}
	    	investor.setAngellist_url(dbObject.get(InvestorEnum.ANGLELIST_URL.getLabel().toString()).toString());
	    	if(!dbObject.get(InvestorEnum.TWITTER_URL.getLabel().toString()).toString().equals(""))
	    	{
	    		investor.setTwitter_url(dbObject.get(InvestorEnum.TWITTER_URL.getLabel().toString()).toString());
	    	}
	    	else
	    	{
	    		investor.setTwitter_url("https://twitter.com/");
	    	}
	    	if(!dbObject.get(InvestorEnum.LINKEDIN_URL.getLabel().toString()).toString().equals(""))
	    	{
	    		investor.setLinkedin_url(dbObject.get(InvestorEnum.LINKEDIN_URL.getLabel().toString()).toString());
	    	}
	    	else
	    	{
	    		investor.setLinkedin_url("http://www.linkedin.com/");
	    	}
	    
	    	investor.setCrunchbase_url(dbObject.get(InvestorEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
	    	
	    	String overview = dbObject.get(InvestorEnum.OVERVIEW.getLabel().toString()).toString();
	    	overview = overview.replaceAll("\\<.*?>","");
	    	overview = overview.replace("&#8217;","'");
	    	overview = overview.replaceAll("&#82..;","");
	    	overview = overview.replaceAll("&nbsp;","");
	    	overview = overview.replaceAll("&amp;","&");
	    	investor.setOverview(overview);
	    	
	    	investor.setStar_score(Double.valueOf(dbObject.get(InvestorEnum.STAR_SCORE.getLabel().toString()).toString()));
	    	investor.setAverage_roi(Double.valueOf(dbObject.get(InvestorEnum.AVERAGE_ROI.getLabel().toString()).toString()));
	    	investor.setFl_norm(Double.valueOf(dbObject.get(InvestorEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString()).toString()));
	    	investor.setCc_norm(Double.valueOf(dbObject.get(InvestorEnum.NORMALIZED_COMPANY_SCORE.getLabel().toString()).toString()));
	    	
	    	investor.setCompaniesInvestedIn((List<String>) dbObject.get(InvestorEnum.COMPANIES_INVESTED_IN.getLabel().toString()));
	    	List<BasicDBObject> fundObjects = (List<BasicDBObject>) dbObject.get(InvestorEnum.FUND_INFO.getLabel().toString());
			List<Fund> funds = new ArrayList<Fund>();
			if (fundObjects != null) 
			{
				for (BasicDBObject fund : fundObjects) 
				{
					Fund fundInfo = new Fund(fund);
					funds.add(fundInfo);
				}
			}
			Collections.sort(funds, new FundComparator());
			investor.setFund_info(funds);
			
	     	List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel().toString());
	    	List<Location> locations = new ArrayList<Location>();
	    	if(locationObjects !=null)
	    	{
	        	for(BasicDBObject location : locationObjects)
	        	{
	        		try {
						JSONObject locObj = new JSONObject(location.toString());
						Location loc = new Location(locObj, LocationEnum.LOCATION_NAME.getLabel().toString());
						locations.add(loc);
					} catch (JSONException e)
					{
						logger.warn("Error while building investr's location object from database");
					}
	        	}
	    	}
	    	investor.setLocations(locations);
		}
    	
  		return investor;
	}
}