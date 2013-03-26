package com.secondmarket.controller;
import com.secondmarket.core.*;

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



public class RankInvestor {
	
	double weight = 0.75;
		
	void topInvestor(Datastore ds){
		
		RankInvestor mc = new RankInvestor();
		int mfc = mc.maxFcPeople(ds);
		int mci = mc.maxCompInv(ds);

		Query<People> q = ds.createQuery(People.class).filter("company_count >", 45).order("-company_count");
		TreeMap<Double, People> scoreP = new TreeMap<Double, People>(Collections.reverseOrder());
		for(People pl : q){
				scoreP.put(mc.scorePeople(pl, mfc, mci), pl);
		}
		
		int counter = 0;
		for(Map.Entry<Double, People> entry : scoreP.entrySet()){
			System.out.println("score: "+entry.getKey()+"\n"+entry.getValue().info());
			counter++;
			if(counter >4) break;
		}
	}
	
	public void assignScore (Datastore ds) throws ClassNotFoundException {
		RankInvestor ri = new RankInvestor();
		int mfc = ri.maxFcPeople(ds);
		int mci = ri.maxCompInv(ds);

		for (People pl : ds.find(People.class)){
			pl.score = scorePeople(pl, mfc, mci);
			ds.save(pl);
		}
		
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
				double s = (pfc/mfp)*(1-weight) + (pcc/mci)*weight;
				return s;
	}

	
}

