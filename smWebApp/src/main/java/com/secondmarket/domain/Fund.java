package com.secondmarket.domain;

import java.io.Serializable;

public class Fund implements Serializable{

	private static final long serialVersionUID = -5527566248012296042L;
	
	private String round;
	private Double amount;
	private Integer funded_year;
	private Integer funded_month;
	private Integer funded_day;
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getFunded_year() {
		return funded_year;
	}
	public void setFunded_year(Integer funded_year) {
		this.funded_year = funded_year;
	}
	public Integer getFunded_month() {
		return funded_month;
	}
	public void setFunded_month(Integer funded_month) {
		this.funded_month = funded_month;
	}
	public Integer getFunded_day() {
		return funded_day;
	}
	public void setFunded_day(Integer funded_day) {
		this.funded_day = funded_day;
	}
	
	
}
