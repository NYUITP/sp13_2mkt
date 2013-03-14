package com.secondmarket.core;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
//import com.mongodb.MongoClient;



public class MongoControl {
		
	public static void main(String[] args) throws MongoException, IOException, JSONException {
		Mongo mongo = new Mongo("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
		System.out.println("success!");
		
		//JSONParser parser = new JSONParser();
	 	//Object obj = parser.parse(new FileReader("D:\\investor_information.json"));
		//JSONObject js = new JSONObject("{\"startup_invested\":[{\"company_id\":\"39025\",\"company_name\":\"Gobble\"},{\"company_id\":\"33213\",\"company_name\":\"RealTravel\"},{\"company_id\":\"32251\",\"company_name\":\"Knewton\"}],\"investor_bio\":\"Entrepreneur. Product Strategist. Investor.\",\"follower_count\":\"22496\",\"investor_id\":\"19188\",\"investor_name\":\"Reid Hoffman\"}");
		
//		AngelCrunch second = new AngelCrunch();
//		JSONObject Investor = new JSONObject();
//		String investor_info = second.searchAngelInvestor("reid-hoffman");
//		String investor_id = second.getfield(investor_info,"id");
//		String investor_name = second.getfield(investor_info,"name");
//		String investor_bio = second.getfield(investor_info,"bio");
//		String investor_follower_count = second.getfield(investor_info,"follower_count");
//		String start_up_role = second.getStartUpRole(investor_id);
//		HashMap this_id_list = new HashMap();
//		second.hashCompanyList(start_up_role, this_id_list);
//		
//		JSONObject each_investor = new JSONObject();
//		JSONArray startup_invested = new JSONArray();
//		int company_count = 0; 
//		for (Object key:this_id_list.keySet()){
//			JSONObject jobj = new JSONObject();
//			jobj.put("company_id", key.toString());
//			jobj.put("company_name", this_id_list.get(key).toString());
//			startup_invested.put(jobj);
//			company_count++;
//		}
//		
//		each_investor.put("company_count", company_count);
//		each_investor.put("startup_invested",startup_invested);
//		each_investor.put("investor_id", investor_id);
//		each_investor.put("investor_name",investor_name);
//		each_investor.put("investor_bio", investor_bio);
//		each_investor.put("follower_count", investor_follower_count);

		
//		People user = new People(Investor);
//		//Company comp = new Company(js);
//		
//		ds.save(user);
//		//ds.save(comp);
		
		//int id = 19188;
		//People investor = ds.get(People.class, id);
		//System.out.println(investor.info());
		
		MongoControl mc = new MongoControl();
		People p = mc.searchId(153,ds);
		int mfc = mc.maxFcPeople(ds);
		int mci = mc.maxCompInv(ds);
		
//		System.out.println(p.info());
//		System.out.println(mfc);
//		System.out.println(mci);
//		System.out.println(mc.scorePeople(p, mfc, mci));
		
		Query<People> q = ds.createQuery(People.class).filter("company_count >", 45).order("-company_count");
		TreeMap<Double, People> scoreP = new TreeMap<Double, People>(Collections.reverseOrder());
		for(People pl : q){
			//System.out.println(pl.info());
			scoreP.put(mc.scorePeople(pl, mfc, mci), pl);
		}
		
		int counter = 0;
		for(Map.Entry<Double, People> entry : scoreP.entrySet()){
			System.out.println("score: "+entry.getKey()+"\n"+entry.getValue().info());
			counter++;
			if(counter >4) break;
		}
	}
	
	
	/*
	 * Return People instance by id
	 */
	People searchId(int id, Datastore ds){
		return ds.get(People.class, id);
	}
	
	int maxFcPeople(Datastore ds){
		Query q = ds.createQuery(People.class).filter("follower_count >", 22500).order("-follower_count");
		People p = (People) q.get();
		return p.follower_count;
	}

	int maxCompInv(Datastore ds){
		Query q = ds.createQuery(People.class).filter("company_count >", 45).order("-company_count");
		People p = (People) q.get();
		return p.company_count;
	}
	  
	double scorePeople(People p, int mfp, int mci){
				double pfc = (double)p.follower_count;
				double pcc = (double)p.company_count;
				double s = (pfc/mfp)*0.25 + (pcc/mci)*0.75;
				return s;
	}

	
}

