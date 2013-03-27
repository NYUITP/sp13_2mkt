package com.secondmarket.domain;

import java.io.Serializable;
import java.util.List;

public class Company implements Serializable{

private static final long serialVersionUID = -5527566248012296042L;
	
	private Integer _id;
	private String name;
	private Integer follower_count;
	private Double total_funding;
	private String angellist_url;
	private Integer quality;
	private String product_desc;
	private List<String> markets;
	private List<String> locations;
	private Fund fund_info;
	private List<Investor> investor;
	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
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
	public Double getTotal_funding() {
		return total_funding;
	}
	public void setTotal_funding(Double total_funding) {
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
	public Fund getFund_info() {
		return fund_info;
	}
	public void setFund_info(Fund fund_info) {
		this.fund_info = fund_info;
	}
	public List<Investor> getInvestor() {
		return investor;
	}
	public void setInvestor(List<Investor> investor) {
		this.investor = investor;
	}
}
