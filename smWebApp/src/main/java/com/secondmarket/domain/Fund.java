package com.secondmarket.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class Fund
{
	private String round;
	private Double amount;
	private Integer funded_year;
	private Integer funded_month;
	private Integer funded_day;
	
	public Fund(JSONObject js) throws JSONException
	{
		round = js.getString(FundEnum.ROUND_CODE.getLabel().toString());
		amount = js.getDouble(FundEnum.RAISED_AMOUNT.getLabel().toString());
		funded_year = js.getInt(FundEnum.YEAR.getLabel().toString());
		funded_month = js.getInt(FundEnum.MONTH.getLabel().toString());
		funded_day = js.getInt(FundEnum.DAY.getLabel().toString());
	}
	
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
}
