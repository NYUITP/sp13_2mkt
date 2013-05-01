package com.secondmarket.common;

public enum InvestorEnum {
	ID("id"),
	_ID("_id"),
    NAME("name"),
    PERMALINK("permalink"),
    FIRST_NAME("first_name"),
	LAST_NAME("last_name"),
	OVERVIEW("overview"),
	FOLLOWER_COUNT("follower_count"),
	COMPANY_COUNT("company_count"),
    INVESTOR_IMAGE("image"),
    ANGLELIST_URL("angellist_url"),
    TWITTER_URL("twitter_url"),
    LINKEDIN_URL("linkedin_url"),
    CRUNCHBASE_URL("crunchbase_url"),
    TWITTER_USERNAME("twitter_username"),
    STAR_SCORE("star_score"),
    AVERAGE_ROI("average_roi"),
	NORMALIZED_COMPANY_SCORE("cc_norm"),
	NORMALIZED_FOLLOWER_SCORE("fl_norm"),
	INVESTMENTS("investments"),
	LOCATIONS("locations"),
	FUND_INFO("fund_info"),
	FUND_ROUNDS("funding_round"),
	BIO("bio"),
	COMPANIES_INVESTED_IN("companiesInvestedIn"),
	CEO("ceo");
	
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
