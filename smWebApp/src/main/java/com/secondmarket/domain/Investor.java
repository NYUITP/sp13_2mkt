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
import com.secondmarket.common.InvestorEnum;
import com.secondmarket.common.LocationEnum;
import com.secondmarket.core.AngelCrunch;

@Entity
public class Investor {
	@Id
	private Integer id;
	private String type;
	private String name;
	private String bio;
	private Integer follower_count;
	private Integer company_count;
	private String image;
	private double fl_norm;
	private double cc_norm;
	private String angellist_url;
	private String blog_url;
	private String twitter_url;
	private String facebook_url;
	private String linkedin_url;
	private String permalink;
	private String first_name;
	private String last_name;
	private String crunchbase_url;
	private String birthplace;
	private String twitter_username;
	private String born_year;
	private String born_month;
	private String born_day;
	private double average_roi = 0.0;

	private String homepage_url;
	private String phone_number;
	private String description;
	private String email_address;
	private String number_of_employees;
	private String founded_year;
	private String founded_month;
	private String founded_day;

	private List<Integer> company_id = new ArrayList<Integer>();
	@Embedded
	private List<Location> locations = new ArrayList<Location>();

	public Investor() {
	}

	public Investor(JSONObject js) throws JSONException {
		type = js.getString("type");
		if (type.equals("people") || type.equals("companies")) {
			id = js.getInt(InvestorEnum.ID.getLabel().toString());
			name = js.getString(InvestorEnum.NAME.getLabel().toString());
			bio = js.getString(InvestorEnum.BIO.getLabel().toString());
			follower_count = js.getInt(InvestorEnum.FOLLOWER_COUNT.getLabel()
					.toString());
			company_count = js.getInt(InvestorEnum.COMPANY_COUNT.getLabel()
					.toString());
			image = js.getString(InvestorEnum.INVESTOR_IMAGE.getLabel()
					.toString());
			angellist_url = js.getString(InvestorEnum.ANGLELIST_URL.getLabel()
					.toString());
			blog_url = js
					.getString(InvestorEnum.BLOG_URL.getLabel().toString());
			twitter_url = js.getString(InvestorEnum.TWITTER_URL.getLabel()
					.toString());
			facebook_url = js.getString(InvestorEnum.FB_URL.getLabel()
					.toString());
			linkedin_url = js.getString(InvestorEnum.LINKEDIN_URL.getLabel()
					.toString());
			permalink = js.getString(InvestorEnum.PERMALINK.getLabel()
					.toString());
			first_name = js.getString(InvestorEnum.FIRST_NAME.getLabel()
					.toString());
			last_name = js.getString(InvestorEnum.LAST_NAME.getLabel()
					.toString());
			crunchbase_url = js.getString(InvestorEnum.CRUNCHBASE_URL
					.getLabel().toString());
			birthplace = js.getString(InvestorEnum.BIRTHPLACE.getLabel()
					.toString());
			twitter_username = js.getString(InvestorEnum.TWITTER_USERNAME
					.getLabel().toString());
			born_year = js.getString(InvestorEnum.BORN_YEAR.getLabel()
					.toString());
			born_month = js.getString(InvestorEnum.BORN_MONTH.getLabel()
					.toString());
			born_day = js
					.getString(InvestorEnum.BORN_DAY.getLabel().toString());
			JSONArray investor_locations = null;
			if (js.has(LocationEnum.LOCATION.getLabel().toString())) {
				investor_locations = js.getJSONArray(LocationEnum.LOCATION
						.getLabel().toString());
				for (int j = 0; j < investor_locations.length(); j++) {
					JSONObject each_location = investor_locations
							.getJSONObject(j);
					Location location_i = new Location(each_location);
					locations.add(location_i);
				}
			}

			JSONArray startups = js.getJSONArray(InvestorEnum.STARTUP_INVESTED
					.getLabel().toString());
			for (int i = 0; i < startups.length(); i++) {
				JSONObject startup = startups.getJSONObject(i);
				company_id.add(startup.getInt(CompanyEnum.ID.getLabel()
						.toString()));
			}
		} else if (type.equals("financial-organizations")) {
			id = js.getInt(InvestorEnum.ID.getLabel().toString());
			name = js.getString(InvestorEnum.NAME.getLabel().toString());
			follower_count = js.getInt(InvestorEnum.FOLLOWER_COUNT.getLabel()
					.toString());
			company_count = js.getInt(InvestorEnum.COMPANY_COUNT.getLabel()
					.toString());
			image = js.getString(InvestorEnum.INVESTOR_IMAGE.getLabel()
					.toString());
			angellist_url = js.getString(InvestorEnum.ANGLELIST_URL.getLabel()
					.toString());
			blog_url = js
					.getString(InvestorEnum.BLOG_URL.getLabel().toString());
			twitter_url = js.getString(InvestorEnum.TWITTER_URL.getLabel()
					.toString());
			permalink = js.getString(InvestorEnum.PERMALINK.getLabel()
					.toString());
			crunchbase_url = js.getString(InvestorEnum.CRUNCHBASE_URL
					.getLabel().toString());
			twitter_username = js.getString(InvestorEnum.TWITTER_USERNAME
					.getLabel().toString());
			homepage_url = js.getString(InvestorEnum.HOMEPAGE_URL.getLabel()
					.toString());
			phone_number = js.getString(InvestorEnum.PHONE_NUMBER.getLabel()
					.toString());
			description = js.getString(InvestorEnum.DESCRIPTION.getLabel()
					.toString());
			email_address = js.getString(InvestorEnum.EMAIL_ADDRESS.getLabel()
					.toString());
			number_of_employees = js.getString(InvestorEnum.NUMBER_OF_EMPLOYEES
					.getLabel().toString());
			founded_year = js.getString(InvestorEnum.FOUNDED_YEAR.getLabel()
					.toString());
			founded_month = js.getString(InvestorEnum.FOUNDED_MONTH.getLabel()
					.toString());
			founded_day = js.getString(InvestorEnum.FOUNDED_DAY.getLabel()
					.toString());

			// location seems to be a problem
			JSONArray investor_locations = null;
			if (js.has(LocationEnum.LOCATION.getLabel().toString())) {
				investor_locations = js.getJSONArray(LocationEnum.LOCATION
						.getLabel().toString());
				for (int j = 0; j < investor_locations.length(); j++) {
					JSONObject each_location = investor_locations
							.getJSONObject(j);
					Location location_i = new Location(each_location);
					locations.add(location_i);
				}
			}

			JSONArray startups = js.getJSONArray(InvestorEnum.STARTUP_INVESTED
					.getLabel().toString());
			for (int i = 0; i < startups.length(); i++) {
				JSONObject startup = startups.getJSONObject(i);
				company_id.add(startup.getInt(CompanyEnum.ID.getLabel()
						.toString()));
			}
		}

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHomepage_url() {
		return homepage_url;
	}

	public void setHomepage_url(String homepage_url) {
		this.homepage_url = homepage_url;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getNumber_of_employees() {
		return number_of_employees;
	}

	public void setNumber_of_employees(String number_of_employees) {
		this.number_of_employees = number_of_employees;
	}

	public String getFounded_year() {
		return founded_year;
	}

	public void setFounded_year(String founded_year) {
		this.founded_year = founded_year;
	}

	public String getFounded_month() {
		return founded_month;
	}

	public void setFounded_month(String founded_month) {
		this.founded_month = founded_month;
	}

	public String getFounded_day() {
		return founded_day;
	}

	public void setFounded_day(String founded_day) {
		this.founded_day = founded_day;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getCrunchbase_url() {
		return crunchbase_url;
	}

	public void setCrunchbase_url(String crunchbase_url) {
		this.crunchbase_url = crunchbase_url;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getTwitter_username() {
		return twitter_username;
	}

	public void setTwitter_username(String twitter_username) {
		this.twitter_username = twitter_username;
	}

	public String getBorn_year() {
		return born_year;
	}

	public void setBorn_year(String born_year) {
		this.born_year = born_year;
	}

	public String getBorn_month() {
		return born_month;
	}

	public void setBorn_month(String born_month) {
		this.born_month = born_month;
	}

	public String getBorn_day() {
		return born_day;
	}

	public void setBorn_day(String born_day) {
		this.born_day = born_day;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public String getAngellist_url() {
		return angellist_url;
	}

	public void setAngellist_url(String angellist_url) {
		this.angellist_url = angellist_url;
	}

	public String getBlog_url() {
		return blog_url;
	}

	public void setBlog_url(String blog_url) {
		this.blog_url = blog_url;
	}

	public String getTwitter_url() {
		return twitter_url;
	}

	public void setTwitter_url(String twitter_url) {
		this.twitter_url = twitter_url;
	}

	public String getFacebook_url() {
		return facebook_url;
	}

	public void setFacebook_url(String facebook_url) {
		this.facebook_url = facebook_url;
	}

	public String getLinkedin_url() {
		return linkedin_url;
	}

	public void setLinkedin_url(String linkedin_url) {
		this.linkedin_url = linkedin_url;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public double getAverage_roi() {
		return average_roi;
	}

	public void setAverage_roi(Double average_roi) {
		this.average_roi = average_roi;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void setROI(double roi) {
		this.average_roi = roi;
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
				&& (this.name == guest.getName() || (this.name != null && this.name
						.equals(guest.getName())));
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