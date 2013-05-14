package com.secondmarket.batch;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.secondmarket.common.MapUtil;
import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Investor;

@Service("rankingService")
public class RankInvestor 
{
	protected static Logger logger = Logger.getLogger("batch");

	private HashMap<String, Double> investorsScores = new HashMap<String, Double>();
	private HashMap<String, Investor> investorObjectMap = new HashMap<String, Investor>();
	private HashMap<String, Double> finOrgScores = new HashMap<String, Double>();
	private HashMap<String, Financial_Org> finOrgObjectMap = new HashMap<String, Financial_Org>();
	private double weight_for_follower_count = 0.0;
	private double weight_for_company_count = 0.0;
	private double weight_for_roi_avg = 0.0;
	
	public List<Investor> getSortedInvestorBasedOnFC_CC_ROI(String followersImpLevel, String companyImpLevel, 
			String roiImpLevel, List<Investor> investors)
	{
		calculateWeights(followersImpLevel, companyImpLevel, roiImpLevel);
		
		for(Investor investor : investors)
		{
			String permalink = investor.getPermalink();
			double followerCount = investor.getFl_norm();
			double companyCount = investor.getCc_norm();
			double starScore = investor.getStar_score();
			double score = caculateEntityScore(permalink, followerCount, companyCount, starScore);
			investorObjectMap.put(permalink, investor);
			investorsScores.put(permalink, score);
		}
		HashMap<String, Double> sortedMap = MapUtil.sortHashMapOfString(investorsScores);
		List<Investor> sortedInvestorSet= new LinkedList<Investor>();
		
		for(Entry<String, Double> entry : sortedMap.entrySet())
		{
			logger.debug("Permalink is - " + entry.getKey() + " and score is - " + entry.getValue());
			sortedInvestorSet.add(investorObjectMap.get(entry.getKey()));
		}
		return sortedInvestorSet;
	}
	
	public List<Financial_Org> getSortedFinanciaOrgBasedOnFC_CC_ROI(String followersImpLevel, String companyImpLevel, 
			String roiImpLevel, List<Financial_Org> financial_Orgs)
	{
		calculateWeights(followersImpLevel, companyImpLevel, roiImpLevel);
		
		for(Financial_Org financial_Org : financial_Orgs)
		{
			String permalink = financial_Org.getPermalink();
			double followerCount = financial_Org.getFl_norm();
			double companyCount = financial_Org.getCc_norm();
			double starScore = financial_Org.getStar_score();
			double score = caculateEntityScore(permalink, followerCount, companyCount, starScore);
			finOrgObjectMap.put(permalink, financial_Org);
			finOrgScores.put(permalink, score);
		}
		HashMap<String, Double> sortedMap = MapUtil.sortHashMapOfString(finOrgScores);
		List<Financial_Org> sortedFinancialOrgSet= new LinkedList<Financial_Org>();
		
		for(Entry<String, Double> entry : sortedMap.entrySet())
		{
			logger.debug("Permalink is - " + entry.getKey() + " and score is - " + entry.getValue());
			sortedFinancialOrgSet.add(finOrgObjectMap.get(entry.getKey()));
		}
		return sortedFinancialOrgSet;
	}

	private double caculateEntityScore(String permalink, double followerCount, double companyCount, double roiAvg) 
	{
		double score = (followerCount*weight_for_follower_count) + (companyCount*weight_for_company_count) + (roiAvg*weight_for_roi_avg);
		score = score*100.0000;
		return score;
	}

	private void calculateWeights(String followersImpLevel, String companyImpLevel, String roiImpLevel) 
	{
		double weight_for_follower = 0.0;
		double weight_for_company = 0.0;
		double weight_for_roi = 0.0;
		if(followersImpLevel.equals(ImportanceScale.Not_Important.getLabel().toString()))
		{
			weight_for_follower = 0.0;
		}
		else if(followersImpLevel.equals(ImportanceScale.A_Little_Important.getLabel().toString()))
		{
			weight_for_follower = 0.25;
		}
		else if(followersImpLevel.equals(ImportanceScale.Moderately_Important.getLabel().toString()))
		{
			weight_for_follower = 0.50;
		}
		else if(followersImpLevel.equals(ImportanceScale.Important.getLabel().toString()))
		{
			weight_for_follower = 0.75;
		}
		else
		{
			weight_for_follower = 1.0;
		}
		
		if(companyImpLevel.equals(ImportanceScale.Not_Important.getLabel().toString()))
		{
			weight_for_company = 0.0;
		}
		else if(companyImpLevel.equals(ImportanceScale.A_Little_Important.getLabel().toString()))
		{
			weight_for_company = 0.25;
		}
		else if(companyImpLevel.equals(ImportanceScale.Moderately_Important.getLabel().toString()))
		{
			weight_for_company = 0.50;
		}
		else if(companyImpLevel.equals(ImportanceScale.Important.getLabel().toString()))
		{
			weight_for_company = 0.75;
		}
		else
		{
			weight_for_company = 1.0;
		}
		
		if(roiImpLevel.equals(ImportanceScale.Not_Important.getLabel().toString()))
		{
			weight_for_roi = 0.0;
		}
		else if(roiImpLevel.equals(ImportanceScale.A_Little_Important.getLabel().toString()))
		{
			weight_for_roi = 0.25;
		}
		else if(roiImpLevel.equals(ImportanceScale.Moderately_Important.getLabel().toString()))
		{
			weight_for_roi = 0.50;
		}
		else if(roiImpLevel.equals(ImportanceScale.Important.getLabel().toString()))
		{
			weight_for_roi = 0.75;
		}
		else
		{
			weight_for_roi = 1.0;
		}		
		
		/**
		 * Multi-dimension weights scale
		 */
		double x = weight_for_follower;
		double y = weight_for_company;
		double z = weight_for_roi;
		double sum = x+y+z;
		
		weight_for_follower_count = x/sum;
		weight_for_company_count = y/sum;
		weight_for_roi_avg = z/sum;
		
	}
}
