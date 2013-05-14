package com.secondmarket.batch;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.secondmarket.common.MapUtil;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;

@Service("companyRankingService")
public class RankCompany 
{
	protected static Logger logger = Logger.getLogger("batch");
	
	private HashMap<String, Double> companyScores = new HashMap<String, Double>();
	private HashMap<String, Company> companyIdObjectMap = new HashMap<String, Company>();
	private double weight_for_follower_count = 0.0;
	
	public List<Company> getSortedCompanyBasedOnFC(String comfollowersImpLevel, List<Company> companies)
	{
		calculateWeights(comfollowersImpLevel);
		
		for(Company company : companies)
		{
			caculateCompanyScore(company);
		}
		HashMap<String, Double> sortedMap = MapUtil.sortHashMapOfString(companyScores);
		List<Company> sortedCompanySet= new LinkedList<Company>();
		
		for(Entry<String, Double> entry : sortedMap.entrySet())
		{
			logger.debug("Permalink is - " + entry.getKey() + " and score is - " + entry.getValue());
			sortedCompanySet.add(companyIdObjectMap.get(entry.getKey()));
		}
		return sortedCompanySet;
	}

	private void caculateCompanyScore(Company company) 
	{
		String permalink = company.getPermalink();
		double followerCount = company.getFollower_count();
		double score = (followerCount*weight_for_follower_count);
		companyScores.put(permalink, score);
		companyIdObjectMap.put(permalink, company);
	}

	private void calculateWeights(String comfollowersImpLevel) 
	{
		double weight_for_follower = 0.0;
		if(comfollowersImpLevel.equals(ImportanceScale.Not_Important.getLabel().toString()))
		{
			weight_for_follower = 1.0;
		}
		else if(comfollowersImpLevel.equals(ImportanceScale.A_Little_Important.getLabel().toString()))
		{
			weight_for_follower = 1.0;
		}
		else if(comfollowersImpLevel.equals(ImportanceScale.Moderately_Important.getLabel().toString()))
		{
			weight_for_follower = 1.0;
		}
		else if(comfollowersImpLevel.equals(ImportanceScale.Important.getLabel().toString()))
		{
			weight_for_follower = 1.0;
		}
		else
		{
			weight_for_follower = 1.0;
		}
		weight_for_follower_count = weight_for_follower;
	}
	
	public List<Company> companyRankingByFundTime(String periodPast, List<Company> companies)
	{
		logger.debug("Retrieving companies ranking by fund time");
		
		int period=0;
		switch (Integer.valueOf(periodPast)){
		case 1: 
			period = 3;	break;
		case 2:
			period = 6;	break;
		case 3:
			period = 12; break;
		case 4:
			period = 24; break;
		default:
			period = 36;
		}
		
		//Get the start date of funding
		Date current = new Date();  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(current);  
		cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)-period));  
		current = cal.getTime();  
		
		HashMap<String, Double> CompanyFund = new HashMap<String, Double>();
		HashMap<String, Company> companyPermalink = new HashMap<String, Company>();
		List<Company> sortedCompanySet= new LinkedList<Company>();
		
		for(Company company : companies)
		{
			double fund_p = 0.0;
			companyPermalink.put(company.getPermalink(), company);
			
			List<Fund> all_fund = company.getFund_info(); 
			for (Fund each_fund: all_fund)
			{
				@SuppressWarnings("deprecation")
				Date f_date = new Date(each_fund.getFunded_year().intValue()-1900, 
						each_fund.getFunded_month().intValue(), each_fund.getFunded_day().intValue());
				//if in period, add to fund_p
				if(f_date.after(current))
				{
					fund_p += each_fund.getRaised_amount();					
				}
			}
			CompanyFund.put(company.getPermalink(), fund_p);
		}
		HashMap<String, Double> sortedMap = MapUtil.sortHashMapOfString(CompanyFund);
		for(Entry<String, Double> entry : sortedMap.entrySet())
		{
			sortedCompanySet.add(companyPermalink.get(entry.getKey()));
		}
		return sortedCompanySet;
	}
}
