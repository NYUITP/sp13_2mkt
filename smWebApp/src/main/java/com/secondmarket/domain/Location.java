package com.secondmarket.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
//import com.secondmarket.common.FundEnum;
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.LocationEnum;

@Embedded
public class Location {
	public Integer location_id;
	public String location_name;
	public String location_angellist_url;
	
	public Location(JSONObject js) throws JSONException{
		location_id = js.getInt(LocationEnum.LOCATION_ID.getLabel().toString());
		location_name = js.getString(LocationEnum.LOCATION_NAME.getLabel().toString());
		location_angellist_url = js.getString(LocationEnum.LOCATION_ANGELLIST_URL.getLabel().toString());
		
	}

	public Integer getLocation_id() {
		return location_id;
	}

	public void setLocation_id(Integer location_id) {
		this.location_id = location_id;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getLocation_angellist_url() {
		return location_angellist_url;
	}

	public void setLocation_angellist_url(String location_angellist_url) {
		this.location_angellist_url = location_angellist_url;
	}
	
}
