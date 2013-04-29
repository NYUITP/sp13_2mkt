package com.secondmarket.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.secondmarket.common.Financial_OrgEnum;

@Entity
public class Financial_Org 
{
	private Integer id;
	@Id private String permalink;
	private String name;
	private String crunchbase_url;
	private String company_url;
	private String twitter_url;
	private String twitter_username;
	private String description;
	private Integer follower_count;
	private String logo_url;
	private String angellist_url;
	private String overview;
	private Integer company_count;
	private double fl_norm = 0.0;
	private double cc_norm = 0.0;
	private double average_roi = 0.0;
	private boolean foundInAngelList = false;
	private boolean foundInCrunchbase = false;
	private List<String> companiesInvestedIn = new ArrayList<String>();
	private List<Investor> ceo = new ArrayList<Investor>();
	@Embedded
	private List<Location> locations = new ArrayList<Location>();
	@Embedded
	private List<Fund> fund_info = new ArrayList<Fund>();	

	
	public Financial_Org() {}

	public Financial_Org(JSONObject js) throws JSONException
	{
		name = js.get(Financial_OrgEnum.NAME.getLabel().toString()).toString();
		permalink = js.get(Financial_OrgEnum._ID.getLabel().toString()).toString();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCrunchbase_url() {
		return crunchbase_url;
	}

	public void setCrunchbase_url(String crunchbase_url) {
		this.crunchbase_url = crunchbase_url;
	}

	public String getTwitter_url() {
		return twitter_url;
	}

	public void setTwitter_url(String twitter_url) {
		this.twitter_url = twitter_url;
	}

	public String getTwitter_username() {
		return twitter_username;
	}

	public void setTwitter_username(String twitter_username) {
		this.twitter_username = twitter_username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(Integer follower_count) {
		this.follower_count = follower_count;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getAngellist_url() {
		return angellist_url;
	}

	public void setAngellist_url(String angellist_url) {
		this.angellist_url = angellist_url;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public Integer getCompany_count() {
		return company_count;
	}

	public void setCompany_count(Integer company_count) {
		this.company_count = company_count;
	}

	public double getFl_norm() {
		return fl_norm;
	}

	public void setFl_norm(double fl_norm) {
		this.fl_norm = fl_norm;
	}

	public double getCc_norm() {
		return cc_norm;
	}

	public void setCc_norm(double cc_norm) {
		this.cc_norm = cc_norm;
	}

	public List<Investor> getCeo() {
		return ceo;
	}

	public void setCeo(List<Investor> ceo) {
		this.ceo = ceo;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public List<Fund> getFund_info() {
		return fund_info;
	}

	public void setFund_info(List<Fund> fund_info) {
		this.fund_info = fund_info;
	}

	public String getCompany_url() {
		return company_url;
	}

	public void setCompany_url(String company_url) {
		this.company_url = company_url;
	}

	public boolean isFoundInAngelList() {
		return foundInAngelList;
	}

	public void setFoundInAngelList(boolean foundInAngelList) {
		this.foundInAngelList = foundInAngelList;
	}

	public boolean isFoundInCrunchbase() {
		return foundInCrunchbase;
	}

	public void setFoundInCrunchbase(boolean foundInCrunchbase) {
		this.foundInCrunchbase = foundInCrunchbase;
	}
	
	public void addLocation(Location location)
	{
		this.getLocations().add(location);
	}

	public List<String> getCompaniesInvestedIn() {
		return companiesInvestedIn;
	}

	public void setCompaniesInvestedIn(List<String> companiesInvestedIn) {
		this.companiesInvestedIn = companiesInvestedIn;
	}

	public double getAverage_roi() {
		return average_roi;
	}

	public void setAverage_roi(double average_roi) {
		this.average_roi = average_roi;
	}
}