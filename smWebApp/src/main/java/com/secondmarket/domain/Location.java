package com.secondmarket.domain;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.secondmarket.common.LocationEnum;

@Embedded
public class Location 
{
	protected static Logger logger = Logger.getLogger("domain");
	
	public Integer id;
	public String name;
	public String angellist_url;
	
	public Location(JSONObject js)
	{
		try {
			id = js.getInt(LocationEnum.LOCATION_ID.getLabel().toString());
			name = js.getString(LocationEnum.LOCATION_NAME.getLabel().toString()).toUpperCase();
			angellist_url = js.getString(LocationEnum.LOCATION_ANGELLIST_URL.getLabel().toString());
		} catch (JSONException e) 
		{
			logger.warn("Failed to form location object from angel json object");
		}
	}
	
	public Location(JSONObject js, String field)
	{
		try {
			name = js.getString(field).toUpperCase();
		} catch (JSONException e) 
		{
			logger.warn("Failed to form location object from crunch json object");
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAngellist_url() {
		return angellist_url;
	}

	public void setAngellist_url(String angellist_url) {
		this.angellist_url = angellist_url;
	}
}
