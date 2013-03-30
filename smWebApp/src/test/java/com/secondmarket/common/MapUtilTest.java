package com.secondmarket.common;

import java.util.HashMap;
import org.junit.Test;

public class MapUtilTest
{
	@Test
	public void testSortHashMap() 
	{
		HashMap<Integer, Double> test = new HashMap<Integer, Double>();
		test.put(1, 20.0);
		test.put(2, 15.0);
		test.put(3, 35.0);
		test.put(4,  15.0);
		test.put(5, 20.0);
		HashMap<Integer, Double> testSorted  = MapUtil.sortHashMap(test);
		
		for (Integer id : testSorted.keySet())
		{
		    System.out.println(id + " " + testSorted.get(id));
		}
	}
}
