package com.secondmarket.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.secondmarket.common.CompanyEnum;

@Entity
public class Company{

	@Id private Integer id;
	private String name;
	private Integer follower_count;
	private String total_funding;
	private String angellist_url;
	private Integer quality;
	private String product_desc;
	private List<String> markets = new ArrayList<String>();
	private List<String> locations = new ArrayList<String>();
	private List<Investor> investor = new ArrayList<Investor>();
	@Embedded
	private List<Fund> fund_info = new ArrayList<Fund>();	
	
	public Company(){} 
	
	public Company(JSONObject js) throws JSONException
	{
		id = js.getInt(CompanyEnum.ID.getLabel().toString());
		name = js.getString(CompanyEnum.NAME.getLabel().toString());
		total_funding = js.getString(CompanyEnum.TOTAL_FUNDING.getLabel().toString());

		JSONArray fund = js.getJSONArray(CompanyEnum.FUNDING_ROUNDS.getLabel().toString());
		for(int i = 0; i<fund.length(); i++)
		{
			Fund fund_i = new Fund(fund.getJSONObject(i));
			fund_info.add(fund_i);
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
	public Integer getFollower_count() {
		return follower_count;
	}
	public void setFollower_count(Integer follower_count) {
		this.follower_count = follower_count;
	}
	public String getTotal_funding() {
		return total_funding;
	}
	public void setTotal_funding(String total_funding) {
		this.total_funding = total_funding;
	}
	public String getAngellist_url() {
		return angellist_url;
	}
	public void setAngellist_url(String angellist_url) {
		this.angellist_url = angellist_url;
	}
	public Integer getQuality() {
		return quality;
	}
	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getProduct_desc() {
		return product_desc;
	}
	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}
	public List<String> getMarkets() {
		return markets;
	}
	public void setMarkets(List<String> markets) {
		this.markets = markets;
	}
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	public List<Investor> getInvestor() {
		return investor;
	}
	public void setInvestor(List<Investor> investor) {
		this.investor = investor;
	}
	public List<Fund> getFund_info() {
		return fund_info;
	}
	public void setFund_info(List<Fund> fund_info) {
		this.fund_info = fund_info;
	}
}
