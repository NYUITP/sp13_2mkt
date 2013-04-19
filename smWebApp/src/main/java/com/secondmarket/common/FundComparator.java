package com.secondmarket.common;

import java.util.Comparator;

import com.secondmarket.domain.Fund;

public class FundComparator implements Comparator<Fund>
{
	@Override
	public int compare(Fund fund1, Fund fund2) 
	{
		return fund2.getRaised_amount().compareTo(fund1.getRaised_amount());
	}
}
