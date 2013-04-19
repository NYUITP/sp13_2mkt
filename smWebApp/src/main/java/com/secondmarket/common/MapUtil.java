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
	public static LinkedHashMap<String, Double> sortHashMapOfString(HashMap<String, Double> input)
	{
		 List<String> mapKeys = new ArrayList<String>(input.keySet());
		 List<Double> mapValues = new ArrayList<Double>(input.values());
		 
		 Collections.sort(mapValues);
		 Collections.reverse(mapValues);
		 Collections.sort(mapKeys);
		 
		 LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		 Iterator<Double> valueIt = mapValues.iterator();
		 while (valueIt.hasNext()) 
		 {
		     Double val = valueIt.next();
		     Iterator<String> keyIt = mapKeys.iterator();
		        
		     while (keyIt.hasNext()) 
		     {
		    	 String key = keyIt.next();
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
