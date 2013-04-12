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
	private String round_code;
	private Double raised_amount;
	private Integer funded_year;
	private Integer funded_month;
	private Integer funded_day;
	@Embedded 
	private List<Fund_company> Fund_company = new ArrayList<Fund_company>();
	
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


	public List<Fund_company> getFund_company() {
		return Fund_company;
	}


	public void setFund_company(List<Fund_company> Fund_company) {
		this.Fund_company = Fund_company;
	}


	public List<Fund_financial_org> getFund_financial_org() {
		return Fund_financial_org;
	}


	public void setFund_financial_org(List<Fund_financial_org> Fund_financial_org) {
		this.Fund_financial_org = Fund_financial_org;
	}


	public List<Fund_person> getFund_person() {
		return Fund_person;
	}


	public void setFund_person(List<Fund_person> Fund_person) {
		this.Fund_person = Fund_person;
	}


	@Embedded
	private List<Fund_financial_org> Fund_financial_org = new ArrayList<Fund_financial_org>();
	@Embedded
	private List<Fund_person> Fund_person = new ArrayList<Fund_person>();
	
	
	public Fund(JSONObject js) throws JSONException
	{
		round_code = js.getString(FundEnum.ROUND_CODE.getLabel().toString());
		raised_amount = js.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString());
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
				Fund_company Fund_company_i = new Fund_company(each_company);
				Fund_company.add(Fund_company_i);
				}
			}
			
		}
			
		if(js.has(FundEnum.FINANCIAL_ORG.getLabel().toString()) ){
			financial_org = js.getJSONArray(FundEnum.FINANCIAL_ORG.getLabel().toString());
			if (!financial_org.isNull(0)){
				for (int j = 0; j<financial_org.length();j++){
				JSONObject each_financial_org = financial_org.getJSONObject(j);
				Fund_financial_org Fund_financial_org_i = new Fund_financial_org(each_financial_org);
				Fund_financial_org.add(Fund_financial_org_i);
				}
			}
			
		}
		if(js.has(FundEnum.PERSON.getLabel().toString())){
			person = js.getJSONArray(FundEnum.PERSON.getLabel().toString());
			if(!person.isNull(0)){
				for(int j = 0; j<person.length();j++){
				JSONObject each_person = person.getJSONObject(j);
				Fund_person Fund_person_i = new Fund_person(each_person);
				Fund_person.add(Fund_person_i);
				}
			}
			
		}
			
		
			
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
}
	