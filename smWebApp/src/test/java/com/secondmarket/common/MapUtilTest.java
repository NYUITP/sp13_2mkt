package com.secondmarket.common;

import java.util.HashMap;
import org.junit.Test;

public class MapUtilTest
{
	@Test
	public void testSortHashMap() 
	{
		HashMap<String, Double> test = new HashMap<String, Double>();
		test.put("1", 20.0);
		test.put("2", 15.0);
		test.put("3", 35.0);
		test.put("4",  15.0);
		test.put("5", 20.0);
		HashMap<String, Double> testSorted  = MapUtil.sortHashMapOfString(test);
		
		for (String id : testSorted.keySet())
		{
		    System.out.println(id + " " + testSorted.get(id));
		}
	}
}
