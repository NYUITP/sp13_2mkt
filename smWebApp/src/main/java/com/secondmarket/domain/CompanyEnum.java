package com.secondmarket.domain;

public enum CompanyEnum {
	ID("id"),
    NAME("name"),
	FOLLOWER_COUNT("follower_count"),
	TOTAL_FUNDING("total_funding"),
	ANGLELIST_URL("angellist_url"),
	QUALITY("quality"),
	PRODUCT_DESC("product_desc"),
	MARKETS("markets"),
	LOCATIONS("locations");
	
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
