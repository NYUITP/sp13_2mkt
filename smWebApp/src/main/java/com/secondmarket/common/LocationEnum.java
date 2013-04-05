package com.secondmarket.common;

public enum LocationEnum {
	LOCATION("locations"),
    LOCATION_ID("location_id"),
    LOCATION_NAME("location_name"),
	LOCATION_ANGELLIST_URL("location_angellist_url");
	
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
