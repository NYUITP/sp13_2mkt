package com.secondmarket.controller;
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

public class RankInvestorTest {

	@Test
	public void testTopInvestor() throws UnknownHostException{
		Mongo mongo = new Mongo("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
		System.out.println("success!");
		
		RankInvestor mc = new RankInvestor();
		mc.topInvestor(ds);
		
	}

}
