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


@Embedded
public class Fund {
	String round;
	double amount;
	int funded_year;
	int funded_month;
	int funded_day;
	
	
	
	Fund(JSONObject js) throws JSONException{
		round = js.getString("round_code");
		amount = js.getDouble("raised_amount");
		funded_year = js.getInt("funded_year");
		funded_month = js.getInt("funded_month");
		funded_day = js.getInt("funded_day");
	}
}

