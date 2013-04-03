package com.secondmarket.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.secondmarket.common.CompanyEnum;
import com.secondmarket.common.InvestorEnum;

@Entity
public class Investor
{	
	@Id private Integer id;
	private String name;
	private String bio;
	private Integer follower_count;
	private Integer company_count;
	private String image;
	private List<Integer> company_id  = new ArrayList<Integer>();
	
	public Investor(){}
	
	public Investor(JSONObject js) throws JSONException
	{
		id = js.getInt(InvestorEnum.ID.getLabel().toString());
		name = js.getString(InvestorEnum.NAME.getLabel().toString());
		bio = js.getString(InvestorEnum.BIO.getLabel().toString());
		follower_count = js.getInt(InvestorEnum.FOLLOWER_COUNT.getLabel().toString());
		company_count = js.getInt(InvestorEnum.COMPANY_COUNT.getLabel().toString());
		image = js.getString(InvestorEnum.INVESTOR_IMAGE.getLabel().toString());

		JSONArray startups = js.getJSONArray(InvestorEnum.STARTUP_INVESTED.getLabel().toString());
		for(int i = 0; i<startups.length(); i++)
		{
			JSONObject startup = startups.getJSONObject(i);
			company_id.add(startup.getInt(CompanyEnum.ID.getLabel().toString()));
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
	public String getImage_url() {
		return image;
	}

	public void setImage_url(String image) {
		this.image = image;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Investor guest = (Investor) obj;
        return this.id == guest.getId()
               && (this.name == guest.getName() || (this.name != null && this.name.equals(guest.getName())));
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + this.id;      
        return result;
    }
}