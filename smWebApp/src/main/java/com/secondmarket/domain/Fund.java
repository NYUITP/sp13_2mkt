package com.secondmarket.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.secondmarket.common.FundEnum;

@Embedded
public class Fund
{
	private String round;
	private Double amount;
	private Integer funded_year;
	private Integer funded_month;
	private Integer funded_day;
	@Embedded 
	private List<fund_company> fund_company = new ArrayList<fund_company>();
	public String getRound() {
		return round;
	}


	public void setRound(String round) {
		this.round = round;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
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


	public List<fund_company> getFund_company() {
		return fund_company;
	}


	public void setFund_company(List<fund_company> fund_company) {
		this.fund_company = fund_company;
	}


	public List<fund_financial_org> getFund_financial_org() {
		return fund_financial_org;
	}


	public void setFund_financial_org(List<fund_financial_org> fund_financial_org) {
		this.fund_financial_org = fund_financial_org;
	}


	public List<fund_person> getFund_person() {
		return fund_person;
	}


	public void setFund_person(List<fund_person> fund_person) {
		this.fund_person = fund_person;
	}


	@Embedded
	private List<fund_financial_org> fund_financial_org = new ArrayList<fund_financial_org>();
	@Embedded
	private List<fund_person> fund_person = new ArrayList<fund_person>();
	
	
	public Fund(JSONObject js) throws JSONException
	{
		round = js.getString(FundEnum.ROUND_CODE.getLabel().toString());
		amount = js.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString());
		funded_year = js.getInt(FundEnum.YEAR.getLabel().toString());
		funded_month = js.getInt(FundEnum.MONTH.getLabel().toString());
		funded_day = js.getInt(FundEnum.DAY.getLabel().toString());
		JSONArray company = null;
		JSONArray financial_org = null;
		JSONArray person = null;
		
		if(js.has(FundEnum.COMPANY.getLabel().toString())){
			company = js.getJSONArray(FundEnum.COMPANY.getLabel().toString());
			if(!company.isNull(0)){
				for(int j = 0; j<company.length();j++){
				JSONObject each_company = company.getJSONObject(j);
				fund_company fund_company_i = new fund_company(each_company);
				fund_company.add(fund_company_i);
				}
			}
			
		}
			
		if(js.has(FundEnum.FINANCIAL_ORG.getLabel().toString()) ){
			financial_org = js.getJSONArray(FundEnum.FINANCIAL_ORG.getLabel().toString());
			if (!financial_org.isNull(0)){
				for (int j = 0; j<financial_org.length();j++){
				JSONObject each_financial_org = financial_org.getJSONObject(j);
				fund_financial_org fund_financial_org_i = new fund_financial_org(each_financial_org);
				fund_financial_org.add(fund_financial_org_i);
				}
			}
			
		}
		if(js.has(FundEnum.PERSON.getLabel().toString())){
			person = js.getJSONArray(FundEnum.PERSON.getLabel().toString());
			if(!person.isNull(0)){
				for(int j = 0; j<person.length();j++){
				JSONObject each_person = person.getJSONObject(j);
				fund_person fund_person_i = new fund_person(each_person);
				fund_person.add(fund_person_i);
				}
			}
			
		}
			
		
			
	}
}
	