package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Investor;
import com.secondmarket.domain.Location;

@Service("investorFilterService")
public class InvestorFilters {

	protected static Logger logger = Logger.getLogger("batch");
	private InvestorService investorService = new InvestorService();
	private FinancialOrgService financialOrgService = new FinancialOrgService();
	
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
			high = 1.0;
			break;
		case 3 :
			low = 0.4;
			high = 1.0;
			break;
		case 2 : 
			low = 0.2;
			high = 1.0;
			break;
		default :
			low = 0.0;
			high = 1.0;
			break;
		}
		
		for(Investor each : investors){
			if(each.getStar_score() <= high && each.getStar_score() > low){
				items.add(each);
			}
		}
		
		return items;
	}

	public List<Financial_Org> filterByStarFin(String starLevel) {
		logger.debug("Retrieving all institution investors star filter");
		List<Financial_Org> items = new ArrayList<Financial_Org>();
		
		List<Financial_Org> finOrg = financialOrgService.getAll();
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
			high = 1.0;
			break;
		case 3 :
			low = 0.4;
			high = 1.0;
			break;
		case 2 : 
			low = 0.2;
			high = 1.0;
			break;
		default :
			low = 0.0;
			high = 1.0;
			break;
		}
		
		for(Financial_Org each : finOrg){
			if(each.getStar_score() <= high && each.getStar_score() > low){
				items.add(each);
			}
		}
		
		return items;
	}
	
	public List<Investor> filterIndividualInvstorsByLocation(String[] loc) 
	{
		logger.debug("Retrieving all investor location filter");
		List<Investor> items = new ArrayList<Investor>(); 

		List<Investor> investors = investorService.getAll();
		for(Investor investor : investors)
		{
			List<Location> all_locations = investor.getLocations();
			done:for(Location each_location: all_locations)
			{
				String location_name = each_location.getName();
				for (String each : loc) 
				{
					if (each.equals("1")){
						if (location_name.equalsIgnoreCase("san francisco")){
							items.add(investor);
							break done;
						}
					} else if (each.equals("2")){
						if (location_name.equalsIgnoreCase("new york, ny")){
							items.add(investor);
							break done;
						}
					} else if (each.equals("3")){
						if (location_name.equalsIgnoreCase("san jose")){
							items.add(investor);
							break done;
						}
					} else if (each.equals("4")){
						if (!location_name.equalsIgnoreCase("san francisco") &&
								!location_name.equalsIgnoreCase("new york, ny") &&
								!location_name.equalsIgnoreCase("san jose"))
							items.add(investor);
						break done;
					}
				}
			}
		}
		return items; 
	}
	
	public List<Financial_Org> filterInstitutionalInvstorsByLocation(String[] loc) 
	{
		logger.debug("Retrieving all investor location filter");
		List<Financial_Org> items = new ArrayList<Financial_Org>(); 

		List<Financial_Org> financialOrgs = financialOrgService.getAll();
		for(Financial_Org financialOrg : financialOrgs)
		{
			List<Location> all_locations = financialOrg.getLocations();
			done:for(Location each_location: all_locations)
			{
				String location_name = each_location.getName();
				for (String each : loc) 
				{
					if (each.equals("1")){
						if (location_name.equalsIgnoreCase("san francisco")){
							items.add(financialOrg);
							break done;
						}
					} else if (each.equals("2")){
						if (location_name.equalsIgnoreCase("new york, ny")){
							items.add(financialOrg);
							break done;
						}
					} else if (each.equals("3")){
						if (location_name.equalsIgnoreCase("san jose")){
							items.add(financialOrg);
							break done;
						}
					} else if (each.equals("4")){
						if (!location_name.equalsIgnoreCase("san francisco") &&
								!location_name.equalsIgnoreCase("new york, ny") &&
								!location_name.equalsIgnoreCase("san jose"))
							items.add(financialOrg);
						break done;
					}
				}
			}
		}
		return items; 
	}
}
