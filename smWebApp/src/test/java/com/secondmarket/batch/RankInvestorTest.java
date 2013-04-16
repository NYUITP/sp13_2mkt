package com.secondmarket.batch;

import org.junit.Test;

public class RankInvestorTest {

	@Test
	public void testInvestorRanking()
	{
		RankInvestor ranking = new RankInvestor();
		ranking.getSortedInvestorBasedOnFC_CC("2", "2","2");
	}
}