package com.secondmarket.common;

public enum FollowersImpScale {
	Not_Important("1"),
	A_Little_Important("2"),
	Moderately_Important("3"),
	Important("4"),
	Very_Important("5"),;
	
    private String label;
 
    private FollowersImpScale(String label) {
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
