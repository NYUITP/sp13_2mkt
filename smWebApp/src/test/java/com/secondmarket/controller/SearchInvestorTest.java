package com.secondmarket.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import java.net.UnknownHostException;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.secondmarket.controller.RankInvestor;
import com.secondmarket.controller.SearchInvestor;
import com.secondmarket.core.People;

public class SearchInvestorTest {

	@Test
	public void testSearchId() throws UnknownHostException{
		Mongo mongo = new Mongo("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
		System.out.println("success!");
		
		SearchInvestor si = new SearchInvestor();
		People p = si.searchId(153,ds);
		System.out.println(p.info());
		
	}

}
