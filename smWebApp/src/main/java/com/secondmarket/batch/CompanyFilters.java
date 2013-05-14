package com.secondmarket.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.secondmarket.domain.Company;
import com.secondmarket.domain.Location;

@Service("companyFilterService")
public class CompanyFilters 
{
	protected static Logger logger = Logger.getLogger("batch");
	
	public List<Company> filterByFunds(String checkBoxVal, List<Company> companies) 
	{
		logger.debug("Retrieving all companies fund filter");
		String[] fundRange = checkBoxVal.split(",");
		List<Company> items = new ArrayList<Company>(); 

		for(Company company : companies)
		{
			for (String each : fundRange) 
			{
				if (each.equals("1")) {
					if ((company.getTotal_money_raised()*1000000.0) < 50000)
						items.add(company);
				} else if (each.equals("2")) {
					if ((company.getTotal_money_raised()*1000000.0) < 1000000
							&& (company.getTotal_money_raised()*1000000.0) >= 50000)
						items.add(company);
				} else if (each.equals("3")) {
					if ((company.getTotal_money_raised()*1000000.0) < 3000000
							&& (company.getTotal_money_raised()*1000000.0) >= 1000000)
						items.add(company);
				} else if (each.equals("4")) {
					if ((company.getTotal_money_raised()*1000000.0) < 5000000
							&& (company.getTotal_money_raised()*1000000.0) >= 3000000)
						items.add(company);
				} else if (each.equals("5")) {
					if ((company.getTotal_money_raised()*1000000.0) >= 5000000)
						items.add(company);
				}
			}
		}
		return items; 
	}

	public List<Company> filterByLocation(String checkBoxVal, List<Company> companies) 
	{
		logger.debug("Retrieving all companies location filter");
		String[] loc = checkBoxVal.split(",");
		List<Company> items = new ArrayList<Company>(); 

		for(Company company : companies)
		{
			List<Location> all_locations = company.getLocations();
			done:for(Location each_location: all_locations)
			{
				String location_name = each_location.getName();
				if(location_name != null)
				{
					for (String each : loc) 
					{
						if (each.equals("1")){
							if (location_name.equalsIgnoreCase("san francisco")){
								items.add(company);
								break done;
							}
						} 
						else if (each.equals("2")){
							if (location_name.equalsIgnoreCase("new york, ny")){
								items.add(company);
								break done;
							}
						}
						else if (each.equals("3")){
							if (location_name.equalsIgnoreCase("los Angeles")){
								items.add(company);
								break done;
							}
						}
						else if (each.equals("4")){
							if (location_name.equalsIgnoreCase("Toronto")){
								items.add(company);
								break done;
							}
						}
						else if (each.equals("5")){
							if (location_name.equalsIgnoreCase("London")){
								items.add(company);
								break done;
							}
						}
						else if (each.equals("6")){
							if (location_name.equalsIgnoreCase("Tokyo")){
								items.add(company);
								break done;
							}
						}
						else if (each.equals("7")){
							if (!location_name.equalsIgnoreCase("san francisco") &&
									!location_name.equalsIgnoreCase("new york, ny") &&
									!location_name.equalsIgnoreCase("los Angeles") &&
									!location_name.equalsIgnoreCase("Toronto") &&
									!location_name.equalsIgnoreCase("London")&&
									!location_name.equalsIgnoreCase("Tokyo"))
								items.add(company);
							break done;
						}
					}
				}
			}
		}
		return items; 
	}
	
	public List<Company> filterByType(String checkBoxVal, List<Company> companies) 
	{
		logger.debug("Retrieving all companies type filter");
		String[] type = checkBoxVal.split(",");
		List<Company> items = new ArrayList<Company>(); 

		for(Company company : companies)
		{
			boolean companyType = company.getIsPrivate();
			for (String each : type) 
			{
				if (each.equals("1")){
					if (companyType){
						items.add(company);
					}
				} 
				else if (each.equals("2")){
					if (!companyType){
						items.add(company);
					}
				}
			}
		}
		return items; 
	}
}
