package com.secondmarket.common;

public enum Financial_OrgEnum 
{
	ID("id"),
	_ID("_id"),
	PERMALINK("permalink"),
	NAME("name"),
	CRUNCHBASE_URL("crunchbase_url"),
	COMPANY_URL("company_url"),
	TWITTER_URL("twitter_url"),
	TWITTER_USERNAME("twitter_username"),
	DESCRIPTION("description"),
	FOLLOWER_COUNT("follower_count"),
	LOGO_URL("logo_url"),
	ANGLELIST_URL("angellist_url"),
	OVERVIEW("overview"),
	COMPANY_COUNT("company_count"),
	NORMALIZED_FOLLOWER_SCORE("fl_norm"),
	NORMALIZED_COMPANY_SCORE("cc_norm"),
	CEO("ceo"),
	LOCATIONS("locations"),
	OFFICES("offices"),
	INVESTMENTS("investments"),
	FUND_ROUNDS("funding_round"),
	FUND_INFO("fund_info"),
	HOME_PAGE_URL("homepage_url"),
	COMPANIES_INVESTED_IN("companiesInvestedIn"),
	STAR_SCORE("star_score"),
	AVERAGE_ROI("average_roi"),
	FUNDING_ROUNDS("fund_info");
	
    private String label;
 
    private Financial_OrgEnum(String label) {
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
