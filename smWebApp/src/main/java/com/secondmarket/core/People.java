package com.secondmarket.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;



@Entity
public class People {
	
	@Id int id;
	String name;
	String bio;
	public int follower_count;
	public int company_count;
	//int investor_follower_count;
	public double fl_norm = 0;
	public double cc_norm = 0;
	
	List<Integer> company_id =new ArrayList<Integer>();
	//@Reference
	//List <Company> comp = new ArrayList<Company>();
	
	/*
	 * Use JSONObject to create a record in collection People in MongoDB
	 */
	People(JSONObject js) throws JSONException{
		id = js.getInt("investor_id");
		name = js.getString("investor_name");
		bio = js.getString("investor_bio");
		follower_count = js.getInt("follower_count");
		company_count = js.getInt("company_count");
		//investor_follower_count = js.getInt("investor_follower_count");
		
		JSONArray temp = js.getJSONArray("startup_invested");
		for(int i = 0; i<temp.length(); i++){
			JSONObject each = temp.getJSONObject(i);
			company_id.add(each.getInt("company_id"));
		}
			
		//while(js.getJSONArray("startup_invested")!=null){
		//	comp.add();
		//}
	}
	
	People(){
	}
	

	
	public String info(){
		String s ="user_info:\n id: "+id+"\n name: "+name+"\n";
		return s;
	}

}

