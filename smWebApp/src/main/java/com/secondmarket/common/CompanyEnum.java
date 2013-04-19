package com.secondmarket.common;

public enum CompanyEnum {
	ID("id"),
	_ID("_id"),
	PERMALINK("permalink"),
	NAME("name"),
	FOLLOWER_COUNT("follower_count"),
	TOTAL_MONEY_RAISED("total_money_raised"),
	OVERVIEW("overview"),
	ANGLELIST_URL("angellist_url"),
	CRUNCHBASE_URL("crunchbase_url"),
	MARKETS("markets"),
	LOCATIONS("locations"),
	FUND_INFO("fund_info"),
	LOGO_URL("logo_url"),
	COMPANY_URL("company_url"),
	TWITTER_URL("twitter_url"),
	TWITTER_USERNAME("twitter_username"),
	BLOG_URL("blog_url"),
	NORMALIZED_FOLLOWER_SCORE("fl_norm"),
	HOME_PAGE_URL("homepage_url"),
	CEO("ceo"),
	OFFICES("offices"),
	INVESTOR_COUNT("investorCount"),
	ALL_INVESTOR("investorPermalinks"),
	FUNDING_ROUNDS("funding_rounds"),
	PROD_DESC("product_desc");
	
    private String label;
 
    private CompanyEnum(String label) {
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
