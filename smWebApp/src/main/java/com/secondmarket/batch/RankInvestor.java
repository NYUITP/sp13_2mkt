package com.secondmarket.batch;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.secondmarket.common.MapUtil;
import com.secondmarket.domain.Investor;

@Service("rankingService")
public class RankInvestor 
{
	protected static Logger logger = Logger.getLogger("batch");
	private InvestorService investorService = new InvestorService();
	private HashMap<Integer, Double> investorsScores = new HashMap<Integer, Double>();
	private HashMap<Integer, Investor> investorIdObjectMap = new HashMap<Integer, Investor>();
	private double weight_for_follower_count = 0.0;
	private double weight_for_company_count = 0.0;
	private double weight_for_roi_avg = 0.0;
	
	public List<Investor> getSortedInvestorBasedOnFC_CC(String followersImpLevel, String companyImpLevel, String roiImpLevel)
	{
		calculateWeights(followersImpLevel, companyImpLevel, roiImpLevel);
		
		List<Investor> investors = investorService.getAll();
		for(Investor investor : investors)
		{
			caculateInvestorsScore(investor);
		}
		HashMap<Integer, Double> sortedMap = MapUtil.sortHashMap(investorsScores);
		List<Investor> sortedInvestorSet= new LinkedList<Investor>();
		
		for(Entry<Integer, Double> entry : sortedMap.entrySet())
		{
			logger.debug("Id is - " + entry.getKey() + " and score is - " + entry.getValue());
			sortedInvestorSet.add(investorIdObjectMap.get(entry.getKey()));
		}
	
		return sortedInvestorSet;
	}

	private void caculateInvestorsScore(Investor investor) 
	{
		int id = investor.getId();
		double followerCount = investor.getFl_norm();
		double companyCount = investor.getCc_norm();
		double roiAvg = investor.getAverage_roi();
		double score = (followerCount*weight_for_follower_count) + (companyCount*weight_for_company_count) + (roiAvg*weight_for_roi_avg);
		score = score*100.0000;
		investorsScores.put(id, score);
		investorIdObjectMap.put(id, investor);
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
