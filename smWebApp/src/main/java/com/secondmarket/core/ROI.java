package com.secondmarket.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.secondmarket.batch.CompanyService;
import com.secondmarket.batch.FinancialOrgService;
import com.secondmarket.batch.InvestorService;
import com.secondmarket.common.CommonStrings;
import com.secondmarket.common.Financial_OrgEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.MongoDBFactory;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Investor;

public class ROI {
	
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static void cacluclateROIForInvestors()
	{
		DBCollection people = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.PEOPLE_COLL.getLabel().toString()); 
		
		InvestorService investorService = new InvestorService();
		List<Investor> investors = investorService.getAll();
		
		for(Investor investor : investors)
		{
	        double average_roi = calculateROIForIndividualInvestor(investor);
	        logger.debug(investor.getName() + ": " + average_roi);
	        
	        DBObject dbObject = investorService.getdbObject(investor.getPermalink());
	        if(dbObject != null)
	        {
	        	dbObject.put(InvestorEnum.AVERAGE_ROI.getLabel().toString(), Double.valueOf(String.format("%.4f", average_roi)));
	        	people.save(dbObject);
	        }
		}
		
		DBCollection finOrg = MongoDBFactory.getCollection(CommonStrings.DATABASENAME.getLabel().toString(),
				CommonStrings.FINANCIAL_ORG.getLabel().toString()); 
		
		FinancialOrgService financialOrgService = new FinancialOrgService();
		List<Financial_Org> financial_Orgs = financialOrgService.getAll();
		
		for(Financial_Org financial_Org : financial_Orgs)
		{
	        double average_roi = calculateROIForFinancialOrgs(financial_Org);
	        logger.debug(financial_Org.getName() + ": " + average_roi);
	        
	        DBObject dbObject = financialOrgService.getdbObject(financial_Org.getPermalink());
	        if(dbObject != null)
	        {
	        	dbObject.put(Financial_OrgEnum.AVERAGE_ROI.getLabel().toString(), Double.valueOf(String.format("%.4f", average_roi)));
	        	finOrg.save(dbObject);
	        }
		}
	}
		
	 private static double calculateROIForIndividualInvestor(Investor investor){
		
		CompanyService companyService = new CompanyService();
		List<Double> all_roi = new ArrayList<Double>();
		double average_roi = 0.0;
		
		logger.debug("Investor: " + investor.getName());
		
		try
		{
			for(String permalink : investor.getCompaniesInvestedIn())
			{				
				Company company = companyService.get(permalink);
				if(company != null && company.getPermalink() != null)
				{
					Date date_in = null;
					String round_in = new String();
					double fta = 0.0;
					double roi = 0.0;

					for(Fund fund : company.getFund_info())
					{
						if(fund.getInvestors() != null && !fund.getInvestors().isEmpty())
						{
							
							Date date_tmp = null;
							String round_tmp = new String();
							
							for(Investor fp : fund.getInvestors())
							{
								if(fp.getPermalink().equals(investor.getPermalink()))
								{
									round_tmp = fund.getRound_code();
									date_tmp = new Date(fund.getFunded_year().intValue() - 1900, fund.getFunded_month().intValue(), fund.getFunded_day().intValue());
									break;
									
								}
							}
							
							if(date_tmp == null)
								continue;
							else if (date_in == null){
								date_in = date_tmp;
								round_in = round_tmp;
							}else{
								if(date_in.after(date_tmp)){
									date_in = date_tmp;
									round_in = round_tmp;
								}
							}
						}
					}
					
					if(date_in == null){
						continue;
					}else{
						if(round_in.equals("unattributed")){
							for(Fund fund: company.getFund_info()){
								Date fund_date = new Date(fund.getFunded_year().intValue() - 1900, fund.getFunded_month().intValue(), fund.getFunded_day().intValue());
								if(!fund_date.before(date_in)){
									fta += fund.getRaised_amount();
								}
							}
						}else{
							for (Fund fund : company.getFund_info()){
								if(fund.getRound_code().equals(round_in)){
									fta += fund.getRaised_amount();
								}else{
									Date fund_date = new Date(fund.getFunded_year().intValue() - 1900, fund.getFunded_month(), fund.getFunded_day());
									if(!fund_date.before(date_in)){
										fta += fund.getRaised_amount();
									}
								}
							}
						}
					}
					
					roi = fta/company.getTotal_money_raised();
					all_roi.add(roi);
				}
			}
			
			int count = 0;
			double total = 0.0;
			for(double each : all_roi)
			{
				total += each;
				count++;
			}
			
			average_roi = total / count;
			if(Double.isNaN(average_roi)){
				average_roi = 0.0;
			}
			
			logger.debug("Average ROI for - " + investor.getName() + " is - " + average_roi);
			
		}catch(Exception e){
			logger.warn("Error while calculating ROI for - " + investor.getPermalink());
		}
		return average_roi;
	}
	 
	private static double calculateROIForFinancialOrgs(Financial_Org financial_Org)
	{
		CompanyService companyService = new CompanyService();
		List<Double> all_roi = new ArrayList<Double>();
		double average_roi = 0.0;
		
		logger.debug("Financial Org: " + financial_Org.getName());
		
		try
		{
			for(String permalink : financial_Org.getCompaniesInvestedIn())
			{
				Company company = companyService.get(permalink);
				if(company != null && company.getPermalink() != null)
				{
					Date date_in = null;
					String round_in = new String();
					double fta = 0.0;
					double roi = 0.0;
//				
//					List<String> allRoundsInvestedIn = new ArrayList<String>();
//					double totalAmountInRound = 0.0;
//					double totalAmountAfter = 0.0;
//					double roi = 0.0;
//					
					for(Fund fund : company.getFund_info())
					{
						if(fund.getFinacialOrgs() != null && !fund.getFinacialOrgs().isEmpty())
						{
							Date date_tmp = null;
							String round_tmp = new String();
							
							for(Financial_Org fp : fund.getFinacialOrgs())
							{
								if(fp.getPermalink().equals(financial_Org.getPermalink()))
								{
									round_tmp = fund.getRound_code();
									date_tmp = new Date(fund.getFunded_year().intValue() - 1900, fund.getFunded_month().intValue(), fund.getFunded_day().intValue());
									break;
//									allRoundsInvestedIn.add(fund.getRound_code());
								}
							}
							
							if(date_tmp == null){
								continue;
							}else if(date_in == null){
								date_in = date_tmp;
								round_in = round_tmp;
							}else{
								if(date_in.after(date_tmp)){
									date_in = date_tmp;
									round_in = round_tmp;
								}
							}
						}
					}
					
					if(date_in == null){
						continue;
					}else{
						if(round_in.equals("unattributed")){
							for(Fund fund : company.getFund_info()){
								if(fund.getRound_code().equals(round_in)){
									fta += fund.getRaised_amount();
								}else{
									Date fund_date = new Date(fund.getFunded_year().intValue() - 1900, fund.getFunded_month().intValue(), fund.getFunded_day().intValue());
									if(!fund_date.before(date_in)){
										fta += fund.getRaised_amount();
									}
								}
							}
						}
					}
					
					roi = fta/company.getTotal_money_raised();
					all_roi.add(roi);
					
//					if(!allRoundsInvestedIn.isEmpty())
//					{
//						String firstRound = allRoundsInvestedIn.get(0);
//						Fund fundObj = new Fund();
//						for(String round : allRoundsInvestedIn)
//						{
//							int x = fundObj.round_order(firstRound);
//							int y = fundObj.round_order(round);
//							if(y<x)
//							{
//								firstRound = round;
//							}
//						}
//						for(Fund fund : company.getFund_info())
//						{
//							if(fund.getRound_code().equalsIgnoreCase(firstRound))
//							{
//								totalAmountInRound = fund.getRaised_amount();
//							}
//							if(fund.round_before(firstRound))
//							{
//								continue;
//							}
//							else
//							{
//								totalAmountAfter += fund.getRaised_amount();
//							}
//						}
//					}
//					roi = (totalAmountInRound/totalAmountAfter);
//					all_roi.add(roi);
				}
			}
			
			int count = 0;
			double total = 0.0;
			for(double each : all_roi)
			{
				total += each;
				count++;
			}
			
			average_roi = total / count;
			if(Double.isNaN((average_roi))){
				average_roi = 0.0;
			}

			logger.debug("Average ROI for - " + financial_Org.getName() + " is - " + average_roi);
				
			}catch(Exception e){
				logger.warn("Error while calculating ROI for - " + financial_Org.getPermalink());
			}
			return average_roi;
		}
}
