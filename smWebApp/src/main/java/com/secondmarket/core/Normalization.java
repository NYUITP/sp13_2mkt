package com.secondmarket.core;

import java.net.UnknownHostException;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class Normalization {
	
	public static void main(String args[]) throws UnknownHostException{
		System.out.println("Start Normalization!");
		Mongo mongo = new Mongo("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
		System.out.println("success!");
		
		Normalization norm = new Normalization();
		int mfp = norm.maxFcPeople(ds);
		int mci = norm.maxCompInv(ds);
		
		Query<People> q = ds.createQuery(People.class);
		for(People p : q){
			norm.normalize(p, mfp, mci, ds);
		}
		
		System.out.println("Normalization finish!");
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
	
	public void normalize(People p, int mfp, int mci, Datastore ds){
		double pfc = (double)p.follower_count;
		double pcc = (double)p.company_count;
		p.fl_norm = pfc/mfp;
		p.cc_norm = pcc/ mci;
		ds.save(p);
	}

}
