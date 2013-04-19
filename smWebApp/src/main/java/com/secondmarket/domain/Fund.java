package com.secondmarket.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.mongodb.BasicDBObject;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.CrunchbaseNamespace;
import com.secondmarket.common.Financial_OrgEnum;
import com.secondmarket.common.FundEnum;
import com.secondmarket.common.InvestorEnum;

@Embedded
public class Fund
{
	protected static Logger logger = Logger.getLogger("domain");
	
	private String round_code;
	private Double raised_amount = 0.0;
	private Double raised_amount_in_million = 0.0;
	private Integer funded_year = 0;
	private Integer funded_month = 0;
	private Integer funded_day = 0;
	private String allInvestorNames;
	private Map<String, String> uniqueInvestors = new HashMap<String, String>();
	
	@Embedded
	private List<Investor> investors = new ArrayList<Investor>();
	@Embedded
	private List<Company> companies = new ArrayList<Company>();
	@Embedded
	private List<Financial_Org> finacialOrgs = new ArrayList<Financial_Org>();
	
	public Fund(){}
	
	public Fund(JSONObject js) throws JSONException
	{
		if(!js.get(FundEnum.ROUND_CODE.getLabel().toString()).toString().equals("null"))
		{
			round_code = js.get(FundEnum.ROUND_CODE.getLabel().toString()).toString();
		}
		if(!js.get(FundEnum.RAISED_AMOUNT.getLabel().toString()).toString().equals("null"))
		{
			raised_amount = js.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString());
		}
		if(!js.get(FundEnum.YEAR.getLabel().toString()).toString().equals("null"))
		{
			funded_year = js.getInt(FundEnum.YEAR.getLabel().toString());
		}
		if(!js.get(FundEnum.MONTH.getLabel().toString()).toString().equals("null"))
		{
			funded_month = js.getInt(FundEnum.MONTH.getLabel().toString());
		}
		if(!js.get(FundEnum.DAY.getLabel().toString()).toString().equals("null"))
		{
			funded_day = js.getInt(FundEnum.DAY.getLabel().toString());
		}
		
		JSONArray investments = js.getJSONArray(FundEnum.INVESTMENTS.getLabel().toString());
		for(int index = 0; index < investments.length() ; index++)
		{
			JSONObject investment = investments.getJSONObject(index);
			JSONObject companyObj = null;
			JSONObject financialOrgObj = null;
			JSONObject personObj = null;
			
			if(!investment.get(FundEnum.FINANCIAL_ORG.toString()).toString().equals("null"))
			{
				financialOrgObj = investment.getJSONObject(FundEnum.FINANCIAL_ORG.getLabel().toString());
			}
			
			if(!investment.get(FundEnum.PERSON.toString()).toString().equals("null"))
			{
				personObj = investment.getJSONObject(FundEnum.PERSON.getLabel().toString());
			}
			
			if(!investment.get(FundEnum.COMPANY.toString()).toString().equals("null"))
			{
				companyObj = investment.getJSONObject(FundEnum.COMPANY.getLabel().toString());
			}
			if(companyObj !=null)
			{
				Company company = new Company();
				company.setName(companyObj.get(CompanyEnum.NAME.getLabel().toString()).toString());
				company.setPermalink(companyObj.get(CompanyEnum.PERMALINK.getLabel().toString()).toString());
				companies.add(company);
				uniqueInvestors.put(companyObj.get(CompanyEnum.PERMALINK.getLabel().toString()).toString(),
						CrunchbaseNamespace.COMPANY.getLabel().toString());
			}
			
			if(financialOrgObj !=null)
			{
				Financial_Org financial_Org = new Financial_Org();
				financial_Org.setName(financialOrgObj.get(Financial_OrgEnum.NAME.getLabel().toString()).toString());
				financial_Org.setPermalink(financialOrgObj.get(Financial_OrgEnum.PERMALINK.getLabel().toString()).toString());
				finacialOrgs.add(financial_Org);
				uniqueInvestors.put(financialOrgObj.get(Financial_OrgEnum.PERMALINK.getLabel().toString()).toString(), 
						CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString());
			}
			
			if(personObj !=null)
			{
				Investor investor = new Investor();
				String name = personObj.get(InvestorEnum.FIRST_NAME.getLabel().toString()).toString() + " " + 
						personObj.get(InvestorEnum.LAST_NAME.getLabel().toString()).toString();
				investor.setName(name);
				investor.setPermalink(personObj.get(InvestorEnum.PERMALINK.getLabel().toString()).toString());
				investors.add(investor);
				uniqueInvestors.put(personObj.get(InvestorEnum.PERMALINK.getLabel().toString()).toString(), 
						CrunchbaseNamespace.PERSON.getLabel().toString());
			}
		}
	}
	
	public Fund(BasicDBObject fund)
	{
		try 
		{
			JSONObject fundObj = new JSONObject(fund.toString());
			round_code = fundObj.get(FundEnum.ROUND_CODE.getLabel().toString()).toString().toUpperCase();
			raised_amount = fundObj.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString());
			raised_amount_in_million = (raised_amount/1000000.0);
			raised_amount_in_million = Double.valueOf(Double.valueOf(String.format("%.2f", raised_amount_in_million)));
			funded_year = fundObj.getInt(FundEnum.YEAR.getLabel().toString());	
			funded_month = fundObj.getInt(FundEnum.MONTH.getLabel().toString());
			funded_day = fundObj.getInt(FundEnum.DAY.getLabel().toString());
			
			JSONArray personArray = null;
			JSONArray companyArray = null;
			JSONArray finOrgArray = null;
			
			if(fundObj.has(FundEnum.INVESTORS.getLabel().toString()))
			{
				personArray = fundObj.getJSONArray(FundEnum.INVESTORS.getLabel().toString());
			}
			if(fundObj.has(FundEnum.COMPANIES.getLabel().toString()))
			{
				companyArray = fundObj.getJSONArray(FundEnum.COMPANIES.getLabel().toString());
			}
			if(fundObj.has(FundEnum.FINANCIALORGS.getLabel().toString()))
			{
				finOrgArray = fundObj.getJSONArray(FundEnum.FINANCIALORGS.getLabel().toString());
			}
			
			if(personArray != null)
			{
				for(int index = 0; index < personArray.length() ; index++)
				{
					Investor investor = new Investor(personArray.getJSONObject(index));
					investors.add(investor);
				}
			}
			if(companyArray != null)
			{
				for(int index = 0; index < companyArray.length() ; index++)
				{
					Company company = new Company(companyArray.getJSONObject(index));
					companies.add(company);
				}
			}
			if(finOrgArray != null)
			{
				for(int index = 0; index < finOrgArray.length() ; index++)
				{
					Financial_Org finOrg = new Financial_Org(finOrgArray.getJSONObject(index));
					finacialOrgs.add(finOrg);
				}
			}
			
		} catch (JSONException e) {
			logger.warn("Failed to form fun object from BasicDBObject");
		}
	}
	
	/**
	 * return if a round is before the round given in parameter
	 */
	public boolean round_before(String round_code){
		int x = round_order(this.round_code);
		int y = round_order(round_code);
		if(x<y)
			return true;
		else
			return false;		
	}
	
	public int round_order(String round_code){
		
		if(round_code.equals("seed"))
			return 0;
		else if (round_code.equals("angel"))
			return 1;
		else if (round_code.equals("a"))
			return 2;
		else if (round_code.equals("b"))
			return 3;
		else if (round_code.equals("c"))
			return 4;
		else if (round_code.equals("d"))
			return 5;
		else if (round_code.equals("e"))
			return 6;
		else return 7;
	}

	public String getRound_code() {
		return round_code;
	}

	public void setRound_code(String round_code) {
		this.round_code = round_code;
	}

	public Double getRaised_amount() {
		return raised_amount;
	}

	public void setRaised_amount(Double raised_amount) {
		this.raised_amount = raised_amount;
	}

	public Integer getFunded_year() {
		return funded_year;
	}

	public void setFunded_year(Integer funded_year) {
		this.funded_year = funded_year;
	}

	public Integer getFunded_month() {
		return funded_month;
	}

	public void setFunded_month(Integer funded_month) {
		this.funded_month = funded_month;
	}

	public Integer getFunded_day() {
		return funded_day;
	}

	public void setFunded_day(Integer funded_day) {
		this.funded_day = funded_day;
	}

	public List<Investor> getInvestors() {
		return investors;
	}

	public void setInvestors(List<Investor> investors) {
		this.investors = investors;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public List<Financial_Org> getFinacialOrgs() {
		return finacialOrgs;
	}

	public void setFinacialOrgs(List<Financial_Org> finacialOrgs) {
		this.finacialOrgs = finacialOrgs;
	}

	public Map<String, String> getUniqueInvestors() {
		return uniqueInvestors;
	}

	public void setUniqueInvestors(Map<String, String> uniqueInvestors) {
		this.uniqueInvestors = uniqueInvestors;
	}
	
	public void addCompany(Company company)
	{
		this.getCompanies().add(company);
	}

	public Double getRaised_amount_in_million() {
		return raised_amount_in_million;
	}

	public void setRaised_amount_in_million(Double raised_amount_in_million) {
		this.raised_amount_in_million = raised_amount_in_million;
	}

	public String getAllInvestorNames() {
		return allInvestorNames;
	}

	public void setAllInvestorNames(String allInvestorNames) {
		this.allInvestorNames = allInvestorNames;
	}
}
	