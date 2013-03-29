/*package com.secondmarket.core;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.code.morphia.annotations.Embedded;
import com.secondmarket.domain.FundEnum;

@Embedded
public class Fund 
{
	private String round;
	private double amount;
	private int funded_year;
	private int funded_month;
	private int funded_day;
	
	public Fund(JSONObject js) throws JSONException{
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getFunded_year() {
		return funded_year;
	}

	public void setFunded_year(int funded_year) {
		this.funded_year = funded_year;
	}

	public int getFunded_month() {
		return funded_month;
	}

	public void setFunded_month(int funded_month) {
		this.funded_month = funded_month;
	}

	public int getFunded_day() {
		return funded_day;
	}

	public void setFunded_day(int funded_day) {
		this.funded_day = funded_day;
	}
}

*/