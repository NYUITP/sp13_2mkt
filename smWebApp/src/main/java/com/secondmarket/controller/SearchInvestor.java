package com.secondmarket.controller;

import com.google.code.morphia.Datastore;
import com.secondmarket.core.People;

public class SearchInvestor {
	
	/*
	 * Return People instance by id
	 */
	public People searchId(int id, Datastore ds){
		return ds.get(People.class, id);
	}

}
