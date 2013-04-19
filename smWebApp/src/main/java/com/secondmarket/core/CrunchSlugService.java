package com.secondmarket.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CrunchSlugService 
{
	protected static Logger logger = Logger.getLogger("batch"); 
	
	private static String personFileName = "personSlug.csv";
	private static String companyFileName = "companySlug.csv";
	private static String nonIpoCompanyFileName = "nonIpoCompanySlug.csv";
	private static String financialOrgFileName = "financialOrgSlug.csv";
	
	public static void main(String args[]) throws IOException, JSONException
	{
		PropertyConfigurator.configure("log4j.properties");
		
		createPersonSlugFile();
		logger.debug("Person completed!!");
		createCompanySlugFile();
		logger.debug("Company completed!!");
		createFinancialOrgSlugFile();
		logger.debug("Financial Org completed!!");
		filterNonIpoCompanies();
		logger.debug("Private company completed!!");
	}

	private static void filterNonIpoCompanies() 
	{
		CSVReader reader;
		CSVWriter writer;
		
		try 
		{
			reader = new CSVReader(new FileReader(companyFileName));
			writer = new CSVWriter(new FileWriter(nonIpoCompanyFileName));
			
			List<String[]> allCompanies = reader.readAll();
			for(String[] company : allCompanies)
			{
				String permlink = company[1];
				boolean isPrivateCompany = isPrivateCompany(permlink);
				if(isPrivateCompany)
				{
					writer.writeNext(company);
				}
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isPrivateCompany(String permlink) 
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String object = "";
		boolean isPrivateCompany = false;
		
		try{
			url = new URL("http://api.crunchbase.com/v/1/company/"+permlink+".js?api_key=3ustn87zw3gqxzsgcnqbp29n");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine())!=null)
			{
				object += line;
			}
			JSONObject company = new JSONObject(object);
			if(company.get("ipo").toString().equals("null"))
			{
				isPrivateCompany = true;
			}
			else
			{
				logger.debug("IPO value for the company is - " + company.get("ipo").toString());
			}
			rd.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return isPrivateCompany;
	}

	private static void createFinancialOrgSlugFile()
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		CSVWriter writer;
		String line;
		
		try{
			writer = new CSVWriter(new FileWriter(financialOrgFileName));
			url = new URL("http://api.crunchbase.com/v/1/financial-organizations.js?api_key=3ustn87zw3gqxzsgcnqbp29n");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			int i = 0;
			String financialOrg = "";
			while ((line = rd.readLine())!=null)
			{
				line = line.replace("[", "");
				financialOrg += line;
				i++;
				if(i == 2)
				{
					JSONObject financialOrgObj = new JSONObject(financialOrg);
					String [] financialOrgFields = new String[2];
					financialOrgFields[0] = financialOrgObj.getString("name");
					financialOrgFields[1] = financialOrgObj.getString("permalink");
					writer.writeNext(financialOrgFields);
					i = 0;
					financialOrg = "";
				}
			}
			rd.close();
			writer.flush();
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void createCompanySlugFile() 
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		CSVWriter writer;
		String line;
		
		try{
			writer = new CSVWriter(new FileWriter(companyFileName));
			url = new URL("http://api.crunchbase.com/v/1/companies.js?api_key=3ustn87zw3gqxzsgcnqbp29n");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			int i = 0;
			String company = "";
			while ((line = rd.readLine())!=null)
			{
				line = line.replace("[", "");
				company += line;
				i++;
				if(i == 3)
				{
					JSONObject companyObj = new JSONObject(company);
					String [] companyFields = new String[3];
					companyFields[0] = companyObj.getString("name");
					companyFields[1] = companyObj.getString("permalink");
					if(companyObj.get("category_code").toString() !=null)
					{
						companyFields[2] = companyObj.get("category_code").toString();
					}
					else
					{
						companyFields[2] = "";
					}
					writer.writeNext(companyFields);
					i = 0;
					company = "";
				}
			}
			rd.close();
			writer.flush();
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void createPersonSlugFile() 
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		CSVWriter writer;
		String line;
		
		try{
			writer = new CSVWriter(new FileWriter(personFileName));
			url = new URL("http://api.crunchbase.com/v/1/people.js?api_key=3ustn87zw3gqxzsgcnqbp29n");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			int i = 0;
			String person = "";
			while ((line = rd.readLine())!=null)
			{
				line = line.replace("[", "");
				person += line;
				i++;
				if(i == 3)
				{
					JSONObject personObj = new JSONObject(person);
					String [] personFields = new String[3];
					personFields[0] = personObj.getString("first_name");
					personFields[1] = personObj.getString("last_name");
					personFields[2] = personObj.getString("permalink");
					writer.writeNext(personFields);
					i = 0;
					person = "";
				}
			}
			rd.close();
			writer.flush();
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
