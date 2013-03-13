package com.secondmarket.domain;

public enum FundEnum {
	ROUND("round"),
    AMOUNT("amount"),
	YEAR("funded_year"),
	MONTH("funded_month"),
	DAY("funded_day");
	
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
