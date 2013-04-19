package com.secondmarket.common;

import java.util.Random;

public class RandomIdGenerator 
{
	private static int seed = 9999;
	
	public static int getRandomId()
	{
		Random generator = new Random( seed );
		int r = generator.nextInt();
		seed = r;
		return r;
	}
}
