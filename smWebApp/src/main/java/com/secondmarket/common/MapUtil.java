package com.secondmarket.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
/* 
 * Referred from - http://www.lampos.net/index.php?q=sort-hashmap
 */
public class MapUtil 
{
	public static HashMap<Integer, Double> sortHashMap(HashMap<Integer, Double> input)
	{
		 List<Integer> mapKeys = new ArrayList<Integer>(input.keySet());
		 List<Double> mapValues = new ArrayList<Double>(input.values());
		 
		 Collections.sort(mapValues);
		 Collections.reverse(mapValues);
		 Collections.sort(mapKeys);
		 
		 LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		 Iterator<Double> valueIt = mapValues.iterator();
		 while (valueIt.hasNext()) 
		 {
		     Double val = valueIt.next();
		     Iterator<Integer> keyIt = mapKeys.iterator();
		        
		     while (keyIt.hasNext()) 
		     {
			     Integer key = keyIt.next();
			     String comp1 = input.get(key).toString();
			     String comp2 = val.toString();
			            
			     if (comp1.equals(comp2)){
			     input.remove(key);
			     mapKeys.remove(key);
			     sortedMap.put(key, (Double)val);
			     break;
			  }
		 }
	}
	return sortedMap;  
	}
}
