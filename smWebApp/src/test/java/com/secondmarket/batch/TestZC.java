package com.secondmarket.batch;
//import com.secondmarket.core.JUnit;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.secondmarket.controller.RankInvestor;
import com.secondmarket.controller.SearchInvestor;
import com.secondmarket.core.People;

public class TestZC {

//	@Test
//	public void testSearchId() throws UnknownHostException{
//		Mongo mongo = new Mongo("localhost", 27017);
//		Morphia morphia = new Morphia();
//		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
//		System.out.println("success!");
//		
//		SearchInvestor si = new SearchInvestor();
//		People p = si.searchId(153,ds);
//		System.out.println(p.info());
//		
//	}
//	
//	@Test
//	public void testTopInvestor() throws UnknownHostException{
//		Mongo mongo = new Mongo("localhost", 27017);
//		Morphia morphia = new Morphia();
//		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
//		System.out.println("success!");
//		
//		RankInvestor mc = new RankInvestor();
//		mc.topInvestor(ds);
//		
//	}

	@Test
	public void testAssignScore() throws UnknownHostException, ClassNotFoundException{
		System.out.println("start testAssignScore!");
		Mongo mongo = new Mongo("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
		System.out.println("success!");
		
		RankInvestor ri = new RankInvestor();
		ri.assignScore(ds);
		
		Query<People> q = ds.createQuery(People.class);
			for(People pl : q){
				System.out.println(pl.info());
		}
		
	}
}
