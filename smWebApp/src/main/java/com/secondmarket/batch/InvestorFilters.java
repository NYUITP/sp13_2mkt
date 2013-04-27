package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import java.util.List;

import org.springframework.stereotype.Service;

import com.secondmarket.domain.Investor;

@Service("investorFilterService")
public class InvestorFilters {

	protected static Logger logger = Logger.getLogger("batch");
	private InvestorService investorService = new InvestorService();
	
	public List<Investor> filterByStar(String starLevel) {
		logger.debug("Retrieving all investors star filter");
		List<Investor> items = new ArrayList<Investor>();
		
		List<Investor> investors = investorService.getAll();
		int level = Integer.parseInt(starLevel);
		double low = 0.0;
		double high = 0.0;
		switch(level){
		case 5 : 
			low = 0.8;
			high = 1.0;
			break;
		case 4 :
			low = 0.6;
			high = 0.8;
			break;
		case 3 :
			low = 0.4;
			high = 0.6;
			break;
		case 2 : 
			low = 0.2;
			high = 0.4;
			break;
		default :
			low = 0.0;
			high = 0.2;
			break;
		}
		
		for(Investor each : investors){
			if(each.getAverage_roi() <= high && each.getAverage_roi() > low){
				items.add(each);
			}
		}
		
		return items;
	}

}
