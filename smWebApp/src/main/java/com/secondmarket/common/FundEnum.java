package com.secondmarket.common;

public enum FundEnum {
	ROUND_CODE("round_code"),
	RAISED_AMOUNT("raised_amount"),
	YEAR("funded_year"),
	MONTH("funded_month"),
	DAY("funded_day"),
	INVESTMENTS("investments"),
	COMPANIES("companies"),
	INVESTORS("investors"),
	FINANCIALORGS("finacialOrgs"),
	PERSON("person"),
	COMPANY("company"),
	FINANCIAL_ORG("financial_org");
	
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
