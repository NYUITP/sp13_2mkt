package com.secondmarket.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.secondmarket.batch.CompanyService;
import com.secondmarket.common.CompanyEnum;

@Entity
public class Company
{
	private Integer id;
	@Id private String permalink;
	private String name;
	private Integer follower_count = 0;
	private double total_money_raised = 0.0;
	private String overview;
	private String angellist_url;
	private String crunchbase_url;
	private String logo_url;
	private String company_url;
	private String twitter_url;
	private String product_desc;
	private String blog_url;
	private double fl_norm = 0.0;
	private boolean foundInAngelList = false;
	private boolean foundInCrunchbase = false;
	private boolean isPrivate = false;
	private int investorCount = 0;
	private Map<String, String> investorPermalinks = new HashMap<String, String>();
	private List<Investor> ceo = new ArrayList<Investor>();;
	private List<String> markets = new ArrayList<String>();
	@Embedded
	private List<Fund> fund_info = new ArrayList<Fund>();	
	@Embedded
	private List<Location> locations = new ArrayList<Location>();
	
	public Company(){} 
	
	public Company(JSONObject js) throws JSONException
	{
		name = js.get(CompanyEnum.NAME.getLabel().toString()).toString();
		permalink = js.get(CompanyEnum._ID.getLabel().toString()).toString();
		CompanyService cs = new CompanyService();
		Company com = cs.get(permalink);
		logo_url = com.getLogo_url();
		if(com != null && com.getLogo_url() != null && !com.getLogo_url().toString().equals(""))
		{
			logo_url = com.getLogo_url();
		}
		else
		{
			logo_url = "https://angel.co/images/shared/nopic_startup.png";
		}
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

	public Integer getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(Integer follower_count) {
		this.follower_count = follower_count;
	}

	public double getTotal_money_raised() {
		return total_money_raised;
	}

	public void setTotal_money_raised(double total_money_raised) {
		this.total_money_raised = total_money_raised;
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

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
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

	public double getFl_norm() {
		return fl_norm;
	}

	public void setFl_norm(double fl_norm) {
		this.fl_norm = fl_norm;
	}

	public List<Investor> getCeo() {
		return ceo;
	}

	public void setCeo(List<Investor> ceo) {
		this.ceo = ceo;
	}

	public List<String> getMarkets() {
		return markets;
	}

	public void setMarkets(List<String> markets) {
		this.markets = markets;
	}

	public List<Fund> getFund_info() {
		return fund_info;
	}

	public void setFund_info(List<Fund> fund_info) {
		this.fund_info = fund_info;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getCrunchbase_url() {
		return crunchbase_url;
	}

	public void setCrunchbase_url(String crunchbase_url) {
		this.crunchbase_url = crunchbase_url;
	}

	public String getCompany_url() {
		return company_url;
	}

	public void setCompany_url(String company_url) {
		this.company_url = company_url;
	}

	public String getProduct_desc() {
		return product_desc;
	}

	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}
	
	public void addLocation(Location location)
	{
		this.getLocations().add(location);
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

	public int getInvestorCount() {
		return investorCount;
	}

	public void setInvestorCount(int investorCount) {
		this.investorCount = investorCount;
	}

	public Map<String, String> getInvestorPermalinks() {
		return investorPermalinks;
	}

	public void setInvestorPermalinks(Map<String, String> investorPermalinks) {
		this.investorPermalinks = investorPermalinks;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
}
