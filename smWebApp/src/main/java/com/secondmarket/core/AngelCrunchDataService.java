package com.secondmarket.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AngelCrunchDataService
{
	protected static Logger logger = Logger.getLogger("core"); 
	
	public static JSONObject parseToJSON(String input){
		JSONObject jobj = null;
		if(input != null && !input.equals(""))
		{
			try 
			{
				jobj = new JSONObject(input);
			} catch (JSONException e) {
				logger.warn("Could not parse string to json object - " + input);
			}
		}
		return jobj;	
	}
	
	public static JSONArray parseToJSONArray(String input){
		JSONArray jarray = null;
		if(input != null && !input.equals(""))
		{
			try 
			{
				jarray = new JSONArray(input);
			} catch (JSONException e) {
				logger.warn("Could not parse string to json array - " + input);
			}
		}
		return jarray;	
	}
	
	/**The available namespaces are:
		company
		person
		financial-organization
		product
		service-provider	
	**/
	public static String getObjectFromCrunchbaseUsingPermalink(String namespace, String permalink)
	{
		URL url = null;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		
		if(namespace != null && !namespace.equals("") && permalink != null && !permalink.equals(""))
		{
			try{
				url = new URL("http://api.crunchbase.com/v/1/"+namespace+"/"+permalink+".js?api_key=m97aznucw5d57wk9m5a94ekp");
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = rd.readLine())!=null)
				{
					result += line;
				}
				rd.close();
			} catch(Exception e){
				logger.warn("No object found in crunchbase for this combination of permalink - " + permalink + " and namespace - " + namespace );
				logger.debug("URL formed is - " + url);
			}
		}
		return result;
	}
	
	/**The available type are:
		User
		Startup
	 **/
	public static String getObjectFromAngelUsingSearchQuery(String type, String name)
	{
		URL url = null;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		
		if(type != null && !type.equals("") && name != null && !name.equals(""))
		{
			
			try{
				checkForRateLimitOfAngelList();
				DatabaseService.increaseTheCounter();
				url = new URL("https://api.angel.co/1/search?query="+name+"&type="+type);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = rd.readLine())!=null){
					result += line;
				}
				rd.close();
			} catch(Exception e){
				logger.warn("No object found in angellist for this name and type combination - " + name + "," + type);
				logger.debug("URL formed is - " + url);
			}
		}
		return result;
	}
	
	/**The available type are:
		users
		startups
	 **/
	public static String getObjectFromAngelUsingId(String type, String id)
	{
		URL url = null;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		
		if(type != null && !type.equals("") && id != null && !id.equals(""))
		{
			try{
				checkForRateLimitOfAngelList();
				DatabaseService.increaseTheCounter();
				url = new URL("https://api.angel.co/1/"+type+"/"+id);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = rd.readLine())!=null){
					result += line;
				}
				rd.close();
			} catch(Exception e){
				logger.warn("No object found in angellist for this id and type combination - " + id + "," + type);
				logger.debug("URL formed is - " + url);
			}
		}
		return result;
	}
	
	private static void checkForRateLimitOfAngelList()
	{
		if(DatabaseService.getAngelListCallCounter() == 990)
		{
			try 
			{
				logger.debug("***********Thread is going to sleep for an hour as angel list rate limit has reached***********");
				Thread.sleep(1000 * 60 * 60);
				logger.debug("*********Thread is up and running again*************");
				
				logger.debug("*********Setting counter to zero again*********");
				DatabaseService.setAngelListCallCounter(0);
				
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
