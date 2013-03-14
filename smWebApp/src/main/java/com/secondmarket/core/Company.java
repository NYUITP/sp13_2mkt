package com.secondmarket.core;


import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import org.bson.types.ObjectId;
import java.util.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@Entity
public class Company {
	
	@Id int id;
	String name;
	//int follower_count;
	
	String total_funding;
	//double total_funding;
	//String angellist_url;
	//int quality;
	//String product_desc;
	//List<String> markets = new ArrayList<String>(); //get "markets" array and get "market_name"
	//List<String> locations = new ArrayList<String>(); //get "locations" array and get "location_name"
	
	//String description;
	
	@Embedded
	List<Fund> fund_info = new ArrayList<Fund>();	
	
//	@Reference
//	People investor;
	
	Company(JSONObject js) throws JSONException{
		id = js.getInt("company_id");
		name = js.getString("company_name");
//		angellist_url = js.getString("angellist_url");
		total_funding = js.getString("total_funding");
//		quality = js.getInt("quality");
//		product_desc = js.getString("product_desc");
//		
//		//description = js.getString("description");
//		follower_count = js.getInt("follower_count");
		
		JSONArray temp = js.getJSONArray("funding_rounds");
		for(int i = 0; i<temp.length(); i++){
			Fund fund_i = new Fund(temp.getJSONObject(i));
			fund_info.add(fund_i);
		}
		
//		JSONArray temp = js.getJSONArray("markets");
//		for(int i = 0; i<temp.length(); i++){
//			JSONObject each = temp.getJSONObject(i);
//			markets.add(each.getString("market_name"));
//		}
//		
//		JSONArray temp2 = js.getJSONArray("locations");
//		for(int i = 0; i<temp.length(); i++){
//			JSONObject each = temp2.getJSONObject(i);
//			locations.add(each.getString("location_name"));
//		}
	}

}
