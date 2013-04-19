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
import com.secondmarket.common.Financial_OrgEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Location;

@Service("financialOrgService")
@Transactional

public class FinancialOrgService 
{
	protected static Logger logger = Logger.getLogger("batch");

	public FinancialOrgService() {}

	public List<Financial_Org> getAll() 
	{
		logger.debug("Retrieving all financial orgs");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.FINANCIAL_ORG.getLabel().toString());
		DBCursor cur = coll.find();
		List<Financial_Org> items = new ArrayList<Financial_Org>(); 

		while (cur.hasNext()) 
		{
			DBObject dbObject = cur.next(); 
			Financial_Org financial_Org = getFinancialOrgObject(dbObject);
			items.add(financial_Org); 
		}
		return items; 
	}

	public List<Financial_Org> get(List<String> permalinks) 
	{
		logger.debug("Retrieving all financial orgs for given permalinks");
		List<Financial_Org> items = new ArrayList<Financial_Org>(); 
		BasicDBList docPermalinks = new BasicDBList();
		if(permalinks != null)
		{
			docPermalinks.addAll(permalinks);
			
			DBObject inClause = new BasicDBObject("$in", docPermalinks);
	        DBObject query = new BasicDBObject(Financial_OrgEnum._ID.getLabel().toString(), inClause);
			DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
					CommonStrings.FINANCIAL_ORG.getLabel().toString());// Retrieve
			DBCursor dbCursor = coll.find(query);
	        if (dbCursor != null)
	        {
	            while (dbCursor.hasNext())
	            {
	            	DBObject dbObject = dbCursor.next(); 
	            	Financial_Org financial_Org = getFinancialOrgObject(dbObject);
	    			items.add(financial_Org); 
	            }
	        }
		}
		return items; 
	}
	
	public Financial_Org get(String permalink) 
	{
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.FINANCIAL_ORG.getLabel().toString());// Retrieve
		DBObject doc = new BasicDBObject(); 
		doc.put(Financial_OrgEnum._ID.getLabel().toString(), permalink); 
		DBObject dbObject = coll.findOne(doc); 
		Financial_Org financial_Org = new Financial_Org();
		if(dbObject != null)
		{
			financial_Org = getFinancialOrgObject(dbObject);	
		}
		return financial_Org; 
	}
	
	public DBObject getdbObject( String permalink ) 
	{
		logger.debug("Retrieving an existing fin org given permalink - " + permalink);
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.FINANCIAL_ORG.getLabel().toString()); 
		DBObject doc = new BasicDBObject(); 
		doc.put(Financial_OrgEnum._ID.getLabel().toString(), permalink); 
        DBObject dbObject = coll.findOne(doc);
        return dbObject;
	}
	
	@SuppressWarnings("unchecked")
	private Financial_Org getFinancialOrgObject(DBObject dbObject) 
	{
		Financial_Org finOrg = new Financial_Org();
		if(dbObject != null)
		{
			finOrg.setPermalink(dbObject.get(Financial_OrgEnum._ID.getLabel().toString()).toString());
			finOrg.setId(Integer.valueOf(dbObject.get(Financial_OrgEnum.ID.getLabel().toString()).toString()));
			finOrg.setName(dbObject.get(Financial_OrgEnum.NAME.getLabel().toString()).toString());
			finOrg.setCrunchbase_url(dbObject.get(Financial_OrgEnum.CRUNCHBASE_URL.getLabel().toString()).toString());
			finOrg.setCompany_url(dbObject.get(Financial_OrgEnum.COMPANY_URL.getLabel().toString()).toString());
			finOrg.setTwitter_url(dbObject.get(Financial_OrgEnum.TWITTER_URL.getLabel().toString()).toString());
			finOrg.setFollower_count(Integer.valueOf(dbObject.get(Financial_OrgEnum.FOLLOWER_COUNT.getLabel().toString()).toString()));
			finOrg.setAngellist_url(dbObject.get(Financial_OrgEnum.ANGLELIST_URL.getLabel().toString()).toString());
			finOrg.setLogo_url(dbObject.get(Financial_OrgEnum.LOGO_URL.getLabel().toString()).toString());
			finOrg.setOverview(dbObject.get(Financial_OrgEnum.OVERVIEW.getLabel().toString()).toString());
			finOrg.setCompany_count(Integer.valueOf(dbObject.get(Financial_OrgEnum.COMPANY_COUNT.getLabel().toString()).toString()));
			
			finOrg.setAverage_roi(Double.valueOf(dbObject.get(Financial_OrgEnum.AVERAGE_ROI.getLabel().toString()).toString()));
			finOrg.setFl_norm(Double.valueOf(dbObject.get(Financial_OrgEnum.NORMALIZED_FOLLOWER_SCORE.getLabel().toString()).toString()));
			finOrg.setCc_norm(Double.valueOf(dbObject.get(Financial_OrgEnum.NORMALIZED_COMPANY_SCORE.getLabel().toString()).toString()));
			finOrg.setCompaniesInvestedIn((List<String>) dbObject.get(Financial_OrgEnum.COMPANIES_INVESTED_IN.getLabel().toString()));
			
			List<BasicDBObject> fundObjects = (List<BasicDBObject>) dbObject.get(Financial_OrgEnum.FUND_INFO.getLabel().toString());
			List<Fund> funds = new ArrayList<Fund>();
			if (fundObjects != null) 
			{
				for (BasicDBObject fund : fundObjects) 
				{
					Fund fundInfo = new Fund(fund);
					funds.add(fundInfo);
				}
			}
			finOrg.setFund_info(funds);
			
			List<BasicDBObject> locationObjects = (List<BasicDBObject>) dbObject.get(LocationEnum.LOCATION.getLabel().toString());
			List<Location> locations = new ArrayList<Location>();
			if (locationObjects != null) 
			{
				for (BasicDBObject location : locationObjects) 
				{
					try 
					{
						JSONObject locObj = new JSONObject(location.toString());
						Location loc = new Location(locObj, LocationEnum.LOCATION_NAME.getLabel().toString());
						locations.add(loc);
					} catch (JSONException e){
						logger.warn("Error while building company's location object from database");
					}
				}
			}
			finOrg.setLocations(locations);
		}
		return finOrg;
	}
}
