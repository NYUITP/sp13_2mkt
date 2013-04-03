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
	
	public List<Investor> getSortedInvestorBasedOnFC_CC(String followersImpLevel, String companyImpLevel)
	{
		calculateWeights(followersImpLevel, companyImpLevel);
		
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
		double followerCount = investor.getFollower_count();
		double companyCount = investor.getCompany_count();
		double score = (followerCount*weight_for_follower_count) + (companyCount*weight_for_company_count);
		investorsScores.put(id, score);
		investorIdObjectMap.put(id, investor);
	}

	private void calculateWeights(String followersImpLevel, String companyImpLevel) 
	{
		double weight_for_follower = 0.0;
		double weight_for_company = 0.0;
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
		
		/**
		 * Multi-dimension weights scale
		 */
		double x = weight_for_follower;
		double y = weight_for_company;
		double sum = x+y;
		
		weight_for_follower_count = x/sum;
		weight_for_company_count = y/sum;
		
	}
}
