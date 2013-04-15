package com.secondmarket.common;

public enum InvestorEnum {
	ID("id"),
	_ID("_id"),
    NAME("name"),
    BIO("bio"),
    INVESTOR_IMAGE("image"),
	FOLLOWER_COUNT("follower_count"),
	COMPANY_COUNT("company_count"),
	COMPANY_IDS("company_id"),
	STARTUP_INVESTED("startup_invested"),
	STARTUP_ROLES("startup_roles"),
	ROLE("role"),
	STARTUP("startup"),
	INVESTOR_INFO("Investor_information"),
	NORMALIZED_FOLLOWER_SCORE("fl_norm"),
	ANGLELIST_URL("angellist_url"),
	BLOG_URL("blog_url"),
	TWITTER_URL("twitter_url"),
	FB_URL("facebook_url"),
	LINKEDIN_URL("linkedin_url"),
	NORMALIZED_COMAPNY_SCORE("cc_norm"),
	PERMALINK("permalink"),
	FIRST_NAME("first_name"),
	LAST_NAME("last_name"),
	CRUNCHBASE_URL("crunchbase_url"),
	BIRTHPLACE("birthplace"),
	TWITTER_USERNAME("twitter_username"),
	BORN_YEAR("born_year"),
	BORN_MONTH("born_month"),
	BORN_DAY("born_day"),
	LOGO_URL("logo_url"),
	PHONE_NUMBER("phone_number"),
	DESCRIPTION("description"),
	EMAIL_ADDRESS("email_address"),
	NUMBER_OF_EMPLOYEES("number_of_employees"),
	FOUNDED_YEAR("founded_year"),
	FOUNDED_MONTH("founded_month"),
	FOUNDED_DAY("founded_day"),
	OVERVIEW("overview"),
	HOMEPAGE_URL("homepage_url");
	
    private String label;
 
    private InvestorEnum(String label) {
        this.label = label;
    }
 
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
