package com.secondmarket.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.secondmarket.common.FundEnum;

@Embedded
public class Fund_financial_org {
	public String name;
	public String permalink;
	public String investor_id;
	
	public String getInvestor_id() {
		return investor_id;
	}

	public void setInvestor_id(String investor_id) {
		this.investor_id = investor_id;
	}

	public Fund_financial_org(JSONObject js)
	{
		try {
			name = js.getString(FundEnum.NAME.getLabel().toString());
			permalink = js.getString(FundEnum.PERMALINK.getLabel().toString());
			if(js.has(FundEnum.INVESTOR_ID.getLabel().toString())){
				investor_id = js.getString(FundEnum.INVESTOR_ID.getLabel().toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

}