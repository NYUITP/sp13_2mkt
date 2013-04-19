package com.secondmarket.common;

public enum CrunchbaseNamespace {
	COMPANY("company"),
    PERSON("person"),
    FINANCIAL_ORG("financial-organization");
	
	private String label;
	 
    private CrunchbaseNamespace(String label) {
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
