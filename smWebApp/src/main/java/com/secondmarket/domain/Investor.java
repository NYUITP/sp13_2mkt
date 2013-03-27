package com.secondmarket.domain;

import java.io.Serializable;
import java.util.List;

public class Investor implements Serializable {
	
private static final long serialVersionUID = -5527566248012296042L;
	
	private Integer _id;
	private String name;
	private String bio;
	private Integer follower_count;
	private Integer company_count;
	private List<Integer> company_id;
	
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
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public Integer getFollower_count() {
		return follower_count;
	}
	public void setFollower_count(Integer follower_count) {
		this.follower_count = follower_count;
	}
	public Integer getCompany_count() {
		return company_count;
	}
	public void setCompany_count(Integer company_count) {
		this.company_count = company_count;
	}
	public List<Integer> getCompany_id() {
		return company_id;
	}
	public void setCompany_id(List<Integer> company_id) {
		this.company_id = company_id;
	}

}