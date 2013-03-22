package com.secondmarket.core;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import java.io.*;
import java.util.*;
import java.net.*;

import org.json.*;

public class InitialDB {
	public Datastore initialize() throws IOException{
		Mongo mongo = new Mongo("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "SecondMarket");
		System.out.println("success!");
		return ds;
	}
	
}
