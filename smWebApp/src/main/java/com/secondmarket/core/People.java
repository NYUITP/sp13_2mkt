/*package com.secondmarket.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.secondmarket.domain.CompanyEnum;
import com.secondmarket.domain.InvestorEnum;

@Entity
public class People 
{	
	@Id private int id;
	private String name;
	private String bio;
	private int follower_count;
	private int company_count;
	private double fl_norm = 0;
	private double cc_norm = 0;
	
	public List<Integer> company_id =new ArrayList<Integer>();
	
	
	 * Use JSONObject to create a record in collection People in MongoDB
	 
	public People(JSONObject js) throws JSONException{
		id = js.getInt(InvestorEnum.ID.getLabel().toString());
		name = js.getString(InvestorEnum.NAME.getLabel().toString());
		bio = js.getString(InvestorEnum.BIO.getLabel().toString());
		follower_count = js.getInt(InvestorEnum.FOLLOWER_COUNT.getLabel().toString());
		company_count = js.getInt(InvestorEnum.COMPANY_COUNT.getLabel().toString());
		
		JSONArray temp = js.getJSONArray(InvestorEnum.STARTUP_INVESTED.getLabel().toString());
		for(int i = 0; i<temp.length(); i++)
		{
			JSONObject each = temp.getJSONObject(i);
			company_id.add(each.getInt(CompanyEnum.ID.getLabel().toString()));
		}
	}
	
	public People(){}

	public String info(){
		String s ="user_info:\n id: "+id+"\n name: "+name+"\n";
		return s;
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

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(int follower_count) {
		this.follower_count = follower_count;
	}

	public int getCompany_count() {
		return company_count;
	}

	public void setCompany_count(int company_count) {
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

	public List<Integer> getCompany_id() {
		return company_id;
	}

	public void setCompany_id(List<Integer> company_id) {
		this.company_id = company_id;
	}
}

*/