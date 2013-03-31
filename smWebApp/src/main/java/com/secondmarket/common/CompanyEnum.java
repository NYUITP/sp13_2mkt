package com.secondmarket.common;

public enum CompanyEnum {
	ID("id"),
	_ID("_id"),
	NAME("name"),
	FOLLOWER_COUNT("follower_count"),
	TOTAL_FUNDING("total_funding"),
	ANGLELIST_URL("angellist_url"),
	QUALITY("quality"),
	PRODUCT_DESC("product_desc"),
	MARKETS("markets"),
	LOCATIONS("locations"),
	CRUNCHBASE_URL("crunchbase_url"),
	FUNDING_ROUNDS("funding_rounds"),
	TOTAL_MONEY_RAISED("total_money_raised");
	
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
