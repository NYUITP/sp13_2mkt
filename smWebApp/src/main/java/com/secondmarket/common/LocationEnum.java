package com.secondmarket.common;

public enum LocationEnum {
	LOCATION("locations"),
    LOCATION_ID("id"),
    LOCATION_NAME("name"),
	LOCATION_ANGELLIST_URL("angellist_url");
	
	private String label;
	 
    private LocationEnum(String label) {
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
