/*package com.secondmarket.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.secondmarket.domain.CompanyEnum;

@Entity
public class Company 
{
	protected static Logger logger = Logger.getLogger("core");
	@Id private int id;
	private String name;
	private String total_funding;
	
	@Embedded
	private List<Fund> fund_info = new ArrayList<Fund>();	

	public Company(JSONObject js) throws JSONException{
		id = js.getInt(CompanyEnum.ID.getLabel().toString());
		name = js.getString(CompanyEnum.NAME.getLabel().toString());
		total_funding = js.getString(CompanyEnum.TOTAL_FUNDING.getLabel().toString());
		
		JSONArray temp = js.getJSONArray(CompanyEnum.FUNDING_ROUNDS.getLabel().toString());
		for(int i = 0; i<temp.length(); i++){
			Fund fund_i = new Fund(temp.getJSONObject(i));
			fund_info.add(fund_i);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotal_funding() {
		return total_funding;
	}

	public void setTotal_funding(String total_funding) {
		this.total_funding = total_funding;
	}

	public List<Fund> getFund_info() {
		return fund_info;
	}

	public void setFund_info(List<Fund> fund_info) {
		this.fund_info = fund_info;
	}
}
*/