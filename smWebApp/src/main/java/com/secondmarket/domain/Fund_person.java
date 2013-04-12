package com.secondmarket.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.secondmarket.common.FundEnum;

@Embedded
public class Fund_person {
	public String first_name;
	public String last_name;
	public String permalink;
	public String investor_id;
	
	public Fund_person(JSONObject js)
	{
		try {
			first_name = js.getString(FundEnum.FIRST_NAME.getLabel().toString());
			last_name = js.getString(FundEnum.LAST_NAME.getLabel().toString());
			permalink = js.getString(FundEnum.PERMALINK.getLabel().toString());
			if(js.has(FundEnum.INVESTOR_ID.getLabel().toString())){
				investor_id = js.getString(FundEnum.INVESTOR_ID.getLabel().toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}



	public void setInvestor_id(String investor_id) {
		this.investor_id = investor_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
}
