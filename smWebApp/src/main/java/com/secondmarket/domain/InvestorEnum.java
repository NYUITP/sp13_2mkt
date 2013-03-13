package com.secondmarket.domain;

public enum InvestorEnum {
	ID("id"),
    NAME("name"),
    BIO("bio"),
	FOLLOWER_COUNT("follower_count"),
	COMPANY_COUNT("company_count"),
	COMPANY_IDS("company_id");
	
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
