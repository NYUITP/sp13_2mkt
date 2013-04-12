package com.secondmarket.core;

import com.secondmarket.batch.CompanyService;
import com.secondmarket.batch.InvestorService;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Fund;
import com.secondmarket.domain.Fund_person;
import com.secondmarket.domain.Investor;

import com.google.code.morphia.Datastore;

public class ROI {
	
	/**
	 * A method to calculate ROI for all investors in Investor collection.
	 * Run after Investor and Company collection completed.
	 */
	
	public static void initializeROI(Datastore ds){
		InvestorService is = new InvestorService();
		for(Investor investor : is.getAll()){
			System.out.println(investor.getName()+"'s roi : "+calculateROI(investor));
			investor.setROI(calculateROI(investor));
			ds.save(investor);
		}
	}
		
	/**
	 * A method to calculate ROI for each investor.
	 */
	static double calculateROI(Investor investor){
		
		CompanyService cs = new CompanyService();
		Company company = new Company();
		double roi = 0.0;
		
		for(int cid : investor.getCompany_id()){
			company = cs.get(cid);
			String round_in = new String();
			double fta = 0.0;
			
			for(Fund fund : company.getFund_info()){
				
				for(Fund_person fp : fund.getFund_person()){
					
					if(fp.getInvestor_id().equals(investor.getId())){
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
			
			roi += fta/company.getTotal_funding();
		}
		return roi;
	}
	


}
