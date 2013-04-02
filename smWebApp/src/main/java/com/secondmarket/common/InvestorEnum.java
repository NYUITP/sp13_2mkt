package com.secondmarket.common;

public enum InvestorEnum {
	ID("id"),
	_ID("_id"),
    NAME("name"),
    BIO("bio"),
    INVESTOR_IMAGE("image"),
    LOCATION("location"),
	FOLLOWER_COUNT("follower_count"),
	COMPANY_COUNT("company_count"),
	COMPANY_IDS("company_id"),
	STARTUP_INVESTED("startup_invested"),
	STARTUP_ROLES("startup_roles"),
	STARTUP("startup"),
	INVESTOR_INFO("Investor_information"),
	NORMALIZED_FOLLOWER_SCORE("fl_norm"),
	NORMALIZED_COMAPNY_SCORE("cc_norm");
	
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
