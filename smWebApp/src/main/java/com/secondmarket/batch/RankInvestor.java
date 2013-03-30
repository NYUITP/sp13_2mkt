package com.secondmarket.batch;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.secondmarket.common.FollowersImpScale;
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
	
	public List<Investor> getSortedInvestorBasedOnFC_CC(String followersImpLevel)
	{
		calculateWeights(followersImpLevel);
		
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

	private void calculateWeights(String followersImpLevel) 
	{
		weight_for_follower_count = 0.0;
		if(followersImpLevel.equals(FollowersImpScale.Not_Important.getLabel().toString()))
		{
			weight_for_follower_count = 0.0;
		}
		else if(followersImpLevel.equals(FollowersImpScale.A_Little_Important.getLabel().toString()))
		{
			weight_for_follower_count = 0.25;
		}
		else if(followersImpLevel.equals(FollowersImpScale.Moderately_Important.getLabel().toString()))
		{
			weight_for_follower_count = 0.50;
		}
		else if(followersImpLevel.equals(FollowersImpScale.Important.getLabel().toString()))
		{
			weight_for_follower_count = 0.75;
		}
		else
		{
			weight_for_follower_count = 1.0;
		}
		weight_for_company_count = (1.0 - weight_for_follower_count);
	}
}
