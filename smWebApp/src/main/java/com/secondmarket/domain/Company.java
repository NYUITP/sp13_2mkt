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
import com.secondmarket.common.LocationEnum;

@Entity
public class Company{

	@Id private Integer id;
	private String name;
	private Integer follower_count;
	private double total_funding;
	private String angellist_url;
	private Integer quality;
	private String product_desc;
	private String high_concept;
	private String logo_url;
	private String company_url;
	private String twitter_url;
	private String blog_url;
	private List<String> markets = new ArrayList<String>();
	private List<Investor> investor = new ArrayList<Investor>();
	@Embedded
	private List<Fund> fund_info = new ArrayList<Fund>();	
	
	@Embedded
	private List<Location> locations = new ArrayList<Location>();
	
	public Company(){} 
	
	public Company(JSONObject js) throws JSONException
	{
		id = js.getInt(CompanyEnum.ID.getLabel().toString());
		name = js.getString(CompanyEnum.NAME.getLabel().toString());
		total_funding = js.getDouble(CompanyEnum.TOTAL_FUNDING.getLabel().toString());
		follower_count  = js.getInt(CompanyEnum.FOLLOWER_COUNT.getLabel().toString());
		quality = js.getInt(CompanyEnum.QUALITY.getLabel().toString());
		angellist_url = js.getString(CompanyEnum.ANGLELIST_URL.getLabel().toString());
		product_desc = js.getString(CompanyEnum.PRODUCT_DESC.getLabel().toString());
		total_funding = js.getDouble(CompanyEnum.TOTAL_FUNDING.getLabel().toString());
		high_concept = js.getString(CompanyEnum.HIGH_CONCEPT.getLabel().toString());
		logo_url = js.getString(CompanyEnum.LOGO_URL.getLabel().toString());
		company_url = js.getString(CompanyEnum.COMPANY_URL.getLabel().toString());
		twitter_url = js.getString(CompanyEnum.TWITTER_URL.getLabel().toString());
		blog_url = js.getString(CompanyEnum.BLOG_URL.getLabel().toString());
		
		JSONArray fund = js.getJSONArray(CompanyEnum.FUNDING_ROUNDS.getLabel().toString());
		for(int i = 0; i<fund.length(); i++)
		{
			Fund fund_i = new Fund(fund.getJSONObject(i));
			fund_info.add(fund_i);
		}
		
		JSONArray investor_locations = null;
		if( js.has(LocationEnum.LOCATION.getLabel().toString())){
			investor_locations = js.getJSONArray(LocationEnum.LOCATION.getLabel().toString());
			for(int j = 0; j<investor_locations.length();j++){
				JSONObject each_location = investor_locations.getJSONObject(j);
				Location location_i = new Location(each_location);
				locations.add(location_i);
			}
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
	public double getTotal_funding() {
		return total_funding;
	}
	public void setTotal_funding(double total_funding) {
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
	public List<Investor> getInvestor() {
		return investor;
	}
	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
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

	public String getHigh_concept() {
		return high_concept;
	}

	public void setHigh_concept(String high_concept) {
		this.high_concept = high_concept;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getCompany_url() {
		return company_url;
	}

	public void setCompany_url(String company_url) {
		this.company_url = company_url;
	}

	public String getTwitter_url() {
		return twitter_url;
	}

	public void setTwitter_url(String twitter_url) {
		this.twitter_url = twitter_url;
	}

	public String getBlog_url() {
		return blog_url;
	}

	public void setBlog_url(String blog_url) {
		this.blog_url = blog_url;
	}
}
