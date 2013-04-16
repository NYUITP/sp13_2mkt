package com.secondmarket.core;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secondmarket.batch.CompanyService;
import com.secondmarket.batch.InvestorService;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.FundEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Fund_person;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.Location;

import com.google.code.morphia.Datastore;

public class ROI {
	
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static void main(String args[]) throws UnknownHostException{

		DBCollection people = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.PEOPLE_COLL.getLabel().toString()); // Retrieve collection
		
		DBCursor people_cur = people.find();
		while(people_cur.hasNext())
		{
	        DBObject dbObject = people_cur.next();// Map DBOject to investor
	        Investor investor = getInvestorObjectBasic(dbObject);
	        double average_roi = calculateROI(investor);
	        System.out.println(investor.getName() + ": " + average_roi);
	    	dbObject.put(InvestorEnum.AVERAGE_ROI.getLabel().toString(), Double.valueOf(String.format("%.4f", average_roi)));
	        people.save(dbObject);
		}
		
	}
	/**
	 * A method to calculate ROI for all investors in Investor collection.
	 * Run after Investor and Company collection completed.
	 */
	
	public static void initializeROI(Datastore ds){
		InvestorService is = new InvestorService();
		for(Investor investor : is.getAllBasic()){
			System.out.println(investor.getName()+"'s roi : "+calculateROI(investor));
			double roi = calculateROI(investor);
			investor.setROI(roi);
			ds.save(investor);
		}
	}
		
	/**
	 * A method to calculate ROI for each investor.
	 */
	static private double calculateROI(Investor investor){
		
		Company company = new Company();
		List<Double> all_roi = new ArrayList<Double>();
		double average_roi = 0.0;
		try{
		for(int cid : investor.getCompany_id()){
			
			//for every company investor invested in
		
			company = get(cid);
			String round_in = new String();
			double fta = 0.0;
			double roi = 0.0;
			
			for(Fund fund : company.getFund_info()){
				//for every fund the company get
				for(Fund_person fp : fund.getFund_person()){
					
					if(fp.getInvestor_id().equals(investor.getId())){
						//get the round code which investor start investing in
						round_in = fund.getRound_code();
						break;
					}
				}
			}
			
			if(round_in.equals(null))
				continue;
			else{
				//calculate amount raised in this and after rounds
				for(Fund fund : company.getFund_info()){
					if(fund.round_before(round_in))
						continue;
					else{
						fta += fund.getRaised_amount();
					}
				}
			}
			
			roi = fta/company.getTotal_funding();
			all_roi.add(roi);
		}
		}catch(Exception e){}
			
		
		int count = 0;
		double total = 0.0;
		for(double each : all_roi){
			total += each;
			count++;
		}
		if(count == 0){
			count++;
		}
		average_roi = total / count;
		if(Double.isNaN(average_roi)){
			average_roi = 0.0;
		}
		return average_roi;
	}
	
	@SuppressWarnings("unchecked")
	private static Investor getInvestorObjectBasic(DBObject dbObject) 
	{
		Investor investor = new Investor();
    	investor.setId(Integer.valueOf(dbObject.get(InvestorEnum._ID.getLabel()).toString()));
    	investor.setName(dbObject.get(InvestorEnum.NAME.getLabel()).toString());
    	investor.setCompany_id((ArrayList<Integer>)dbObject.get(InvestorEnum.COMPANY_IDS.getLabel().toString()));	
		return investor;
	}
	
	@SuppressWarnings("unchecked")
	private static Company getCompanyObjectBasic(DBObject dbObject) 
	{
		Company company = new Company();
		company.setId(Integer.valueOf(dbObject.get(CompanyEnum._ID.getLabel()).toString()));
		company.setName(dbObject.get(CompanyEnum.NAME.getLabel()).toString());
		company.setTotal_funding(Double.valueOf(dbObject.get(CompanyEnum.TOTAL_FUNDING.getLabel()).toString()));
		company.setInvestor((ArrayList<Integer>)dbObject.get(CompanyEnum.INVESTORS.getLabel().toString()));	
		
		List<BasicDBObject> fundObjects = (List<BasicDBObject>) dbObject.get(FundEnum.FUND_INFO.getLabel());
		List<Fund> fund_info = new ArrayList<Fund>();
		if (fundObjects != null) {
			for (BasicDBObject fund : fundObjects) {
				try {
					JSONObject fObj = new JSONObject(fund.toString());
					Fund fd = new Fund(fObj);
					fund_info.add(fd);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		company.setFund_info(fund_info);		
		return company;
	}
	
	public static Company get(Integer id) 
	{
		logger.debug("Retrieving an existing Company");
		DBCollection coll = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),CommonStrings.COMPANY_COLL.getLabel().toString());// Retrieve
		DBObject doc = new BasicDBObject(); // Create a new object
		doc.put(CompanyEnum._ID.getLabel().toString(), id); // Put id to search
		DBObject dbObject = coll.findOne(doc); // Find and return the Company
		Company company = getCompanyObjectBasic(dbObject);	
		return company; // Return company
	}


}
