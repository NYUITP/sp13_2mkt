package com.secondmarket.common;

public enum FundEnum {
	FUND_INFO("fund_info"),
	ROUND("round"),
	AMOUNT("amount"),
	YEAR("funded_year"),
	MONTH("funded_month"),
	DAY("funded_day"),
	ROUND_CODE("round_code"),
	RAISED_AMOUNT("raised_amount"),
	INVESTMENTS("investments"),
	COMPANY("company"),
	FINANCIAL_ORG("financial_org"),
	PERSON("person"),
	FIRST_NAME("first_name"),
	LAST_NAME("last_name"),
	NAME("name"),
	PERMALINK("permalink"),
	INVESTOR_ID("investor_id");
	
	
    private String label;
 
    private FundEnum(String label) {
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
