package com.secondmarket.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.secondmarket.batch.CompanyFilters;
import com.secondmarket.batch.CompanyService;
import com.secondmarket.batch.FinancialOrgService;
import com.secondmarket.batch.InvestorFilters;
import com.secondmarket.batch.InvestorService;
import com.secondmarket.batch.RankCompany;
import com.secondmarket.batch.RankInvestor;
import com.secondmarket.common.CrunchbaseNamespace;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Financial_Org;
import com.secondmarket.domain.Investor;

@Controller
@RequestMapping("/")

public class BaseController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	private int recordsPerPage = 50;
	private static List<Company> companyResults = null;
	private static List<Investor> investorResults = null;
	private static List<Financial_Org> financialOrgResults = null;
	
	//company variables
	private static String periodPastVar = "3";
	private static String comfollowersImpLevelVar = "3";
	private static String totalFundingVar = "1,2,3,4,5";
	private static String comLocationVar = "1,2,3,4,5,6,7";
	private static String companyTypeVar = "1,2";
	
	//investor variables
	private static String invFollowersImpLevelVar = "3";
	private static String invCompanyImpLevelVar = "3";
	private static String invRoiImpLevelVar = "3";
	private static String investorLocationVar = "1,2,3,4,5,6,7";
	
	//fin org variables
	private static String finOrgFollowersImpLevelVar = "3";
	private static String finOrgCompanyImpLevelVar = "3";
	private static String finOrgRoiImpLevelVar = "3";
	private static String finOrgLocationVar = "1,2,3,4,5,6,7";
	
	@Resource(name="investorService")
	private InvestorService investorService;
	
	@Resource(name="companyService")
	private CompanyService companyService;
	
	@Resource(name="financialOrgService")
	private FinancialOrgService financialOrgService;
	
	@Resource(name="rankingService")
	private RankInvestor rankedInvestor;
	
	@Resource(name="companyRankingService")
	private RankCompany rankCompany;
	
	@Resource(name="companyFilterService")
	private CompanyFilters companyFilterService;
	
	@Resource(name="investorFilterService")
	private InvestorFilters investorFilterService;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String welcome(ModelMap model) 
	{
		try
		{
			model.addAttribute("message", "Welcome!");
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
		return "index"; 
	}
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String welcomeHome(ModelMap model) {
 
		try
		{
			model.addAttribute("message", "Welcome Home");
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
		return "index";
	}
	
	//*************************************** Summary pages for Company, Investor and Fin Org ************************************************//
	
	@RequestMapping(value="/companies", method = RequestMethod.GET)
	public String getCompanies(@RequestParam("page") int page, ModelMap model) 
	{
		try
		{
			List<Company> companies = companyService.getAllCompanies();
			setCompanyPageParamenters(page, companies, model);
			setDefaultVariableValuesForCompany();
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}

	@RequestMapping(value="/paginateCompanies", method = RequestMethod.GET)
	public String paginateCompanies(@RequestParam("page") int page, ModelMap model) 
	{
		try
		{
			int pageNumber = 1;
	        pageNumber = page;
			int noOfRecords = companyResults.size();
	    	int startIndex= (pageNumber-1)*recordsPerPage;
	    	int endIndex = (noOfRecords - startIndex)>recordsPerPage ? (startIndex + recordsPerPage) : (startIndex + noOfRecords - startIndex);
	    	List<Company> list = companyResults.subList(startIndex, endIndex);
	    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
	    	
	    	model.addAttribute("noOfPages", noOfPages);
	    	model.addAttribute("companies", list);
	    	model.addAttribute("startIndex", startIndex+1);
	    	model.addAttribute("endIndex", endIndex);
	    	model.addAttribute("size", noOfRecords);
	    	model.addAttribute("currentPage", page);
	    	setCompanyVariable(model);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}
	
	@RequestMapping(value="/investors", method = RequestMethod.GET)
	public String getInvestors(@RequestParam("page") int page, ModelMap model) 
	{
		try
		{
			List<Investor> investors = investorService.getAllInvestors();
			setInvestorPageParamenters(page, investors, model);
			setDefaultVariableValuesForInvestor();
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "investorsPage";
	}
	
	@RequestMapping(value="/paginateInvestors", method = RequestMethod.GET)
	public String paginateInvestors(@RequestParam("page") int page, ModelMap model) 
	{
		try
		{
			int pageNumber = 1;
	        pageNumber = page;
	        int noOfRecords = investorResults.size();
	    	int startIndex= (pageNumber-1)*recordsPerPage;
	    	int endIndex = (noOfRecords - startIndex)>recordsPerPage ? (startIndex + recordsPerPage) : (startIndex + noOfRecords - startIndex);
	    	List<Investor> list = investorResults.subList(startIndex, endIndex);
	    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
	    	
	    	model.addAttribute("noOfPages", noOfPages);
	    	model.addAttribute("investors", list);
	    	model.addAttribute("startIndex", startIndex+1);
	    	model.addAttribute("endIndex", endIndex);
	    	model.addAttribute("size", noOfRecords);
	    	model.addAttribute("currentPage", page);
	    	setInvestorVariable(model);
		}
	    catch(Exception ex)
	    {
	    	return "errorPage";
	    }
	    return "investorsPage";
	}

	@RequestMapping(value="/financialOrg", method = RequestMethod.GET)
	public String getFinancialOrg(@RequestParam("page") int page, ModelMap model) 
	{
		try
		{
			List<Financial_Org> finOrgs = financialOrgService.getAllFinancialOrgs();
			setFinancialOrgPageParamenters(page, finOrgs, model);
			setDefaultVariableValuesForFinOrg();
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "financialOrgPage";
	}
	
	@RequestMapping(value="/paginateFinOrgs", method = RequestMethod.GET)
	public String paginateFinOrgs(@RequestParam("page") int page, ModelMap model) 
	{
		try
		{
			int pageNumber = 1;
	        pageNumber = page;
	        int noOfRecords = financialOrgResults.size();
	    	int startIndex= (pageNumber-1)*recordsPerPage;
	    	int endIndex = (noOfRecords - startIndex)>recordsPerPage ? (startIndex + recordsPerPage) : (startIndex + noOfRecords - startIndex);
	    	List<Financial_Org> list = financialOrgResults.subList(startIndex, endIndex);
	    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
	    	
	    	model.addAttribute("noOfPages", noOfPages);
	    	model.addAttribute("finOrgs", list);
	    	model.addAttribute("startIndex", startIndex+1);
	    	model.addAttribute("endIndex", endIndex);
	    	model.addAttribute("size", noOfRecords);
	    	model.addAttribute("currentPage", page);
	    	setFinOrgVariable(model);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "financialOrgPage";
	}
	
	//*************************************************** Ranking Methods ********************************************************//
	
	@RequestMapping(value="/investorRankingByFC_CC_ROI", method = RequestMethod.POST)
	public String getInvestorRanking(@RequestParam("page") int page, @RequestParam("followersImpLevel") String followersImpLevel, 
			@RequestParam("companyImpLevel") String companyImpLevel, @RequestParam("roiImpLevel") String roiImpLevel, ModelMap model) 
	{
		try
		{
			invFollowersImpLevelVar = followersImpLevel;
			invCompanyImpLevelVar = companyImpLevel;
			invRoiImpLevelVar = roiImpLevel;
			List<Investor> investors = getInvestorsToDisplay();
			setInvestorPageParamenters(page, investors, model);
	    	setInvestorVariable(model);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "investorsPage";
	}
	
	@RequestMapping(value="/finOrgRankingByFC_CC_ROI", method = RequestMethod.POST)
	public String getFinancialOrgRanking(@RequestParam("page") int page, @RequestParam("followersImpLevel") String followersImpLevel, 
			@RequestParam("companyImpLevel") String companyImpLevel, @RequestParam("roiImpLevel") String roiImpLevel, ModelMap model) 
	{
		try
		{
			finOrgFollowersImpLevelVar = followersImpLevel;
			finOrgCompanyImpLevelVar = companyImpLevel;
			finOrgRoiImpLevelVar = roiImpLevel;
			List<Financial_Org> financial_Orgs = getFinOrgsToDisplay();
	    	setFinancialOrgPageParamenters(page, financial_Orgs, model);
	    	setFinOrgVariable(model);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
		return "financialOrgPage";
	}
	
	@RequestMapping(value="/companyRankingByFundTime", method = RequestMethod.POST)
	public String getCompanyRankingByFundTime(@RequestParam("page") int page, 
			@RequestParam("periodPast") String periodPast, ModelMap model) 
	{
		try
		{
			periodPastVar = periodPast;
			comfollowersImpLevelVar = "3";
			List<Company> companies = getCompaniesToDisplay(true, false);
			setCompanyPageParamenters(page, companies, model);
			setCompanyVariable(model);
			model.addAttribute("periods", periodPast);
			model.addAttribute("companyfollowerLevel", comfollowersImpLevelVar);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}

	@RequestMapping(value="/companyRankingByFollowers", method = RequestMethod.POST)
	public String getCompanyRankedByFollowers(@RequestParam("page") int page, 
			@RequestParam("comfollowersImpLevel") String comfollowersImpLevel, ModelMap model) 
	{
		try
		{
			comfollowersImpLevelVar = comfollowersImpLevel;
			periodPastVar = "3";
			List<Company> companies = getCompaniesToDisplay(false, true);
			setCompanyPageParamenters(page, companies, model);
			setCompanyVariable(model);
			model.addAttribute("companyfollowerLevel", comfollowersImpLevel);
			model.addAttribute("periods", periodPastVar);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}
	
	@RequestMapping(value="/investorLocationFilter",  method = RequestMethod.POST)
	public String filterInvestorByLocation(@RequestParam("page") int page, @RequestParam("location") String checkBoxVal, ModelMap model) 
	{
		try
		{
			investorLocationVar = checkBoxVal;
	    	List<Investor> investors = getInvestorsToDisplay();
	    	setInvestorPageParamenters(page, investors, model);
	    	setInvestorVariable(model);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "investorsPage";
	}
	
	@RequestMapping(value="/finOrgLocationFilter",  method = RequestMethod.POST)
	public String filterFinancialOrgByLocation(@RequestParam("page") int page, 
			@RequestParam("location") String checkBoxVal, ModelMap model) 
	{
		try
		{
	    	finOrgLocationVar = checkBoxVal;
			List<Financial_Org> finOrgs = getFinOrgsToDisplay();
	    	setFinancialOrgPageParamenters(page, finOrgs, model);
	    	setFinOrgVariable(model);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "financialOrgPage";
	}
	
	@RequestMapping(value="/companyFundFilter",  method = RequestMethod.POST)
	public String filterCompanyByFund(@RequestParam("page") int page, @RequestParam("total_funding") String checkBoxVal, ModelMap model) 
	{
		try
		{
			totalFundingVar = checkBoxVal;
			List<Company> companies = getCompaniesToDisplay(false, false);
	    	setCompanyPageParamenters(page, companies, model);
	    	setCompanyVariable(model);
	    	model.addAttribute("total_funding", checkBoxVal);
	    	model.addAttribute("companyfollowerLevel", comfollowersImpLevelVar);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}
		
	@RequestMapping(value="/companyLocationFilter",  method = RequestMethod.POST)
	public String filterCompanyByLocation(@RequestParam("page") int page, @RequestParam("location") String checkBoxVal, ModelMap model) 
	{
		try
		{
			comLocationVar = checkBoxVal;
			List<Company> companies = getCompaniesToDisplay(false, false);
	    	setCompanyPageParamenters(page, companies, model);
	    	setCompanyVariable(model);
	    	model.addAttribute("location", checkBoxVal);
	    	model.addAttribute("companyfollowerLevel", comfollowersImpLevelVar);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}
	
	@RequestMapping(value="/companyTypeFilter",  method = RequestMethod.POST)
	public String filterCompanyByType(@RequestParam("page") int page, @RequestParam("companyType") String checkBoxVal, ModelMap model) 
	{
		try
		{
			companyTypeVar = checkBoxVal;
			List<Company> companies = getCompaniesToDisplay(false, false);
	    	setCompanyPageParamenters(page, companies, model);
	    	setCompanyVariable(model);
	    	model.addAttribute("companyType", checkBoxVal);
	    	model.addAttribute("companyfollowerLevel", comfollowersImpLevelVar);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyPage";
	}
	//*************************************** Detailed pages for COmpany, Investor and Fin Org ************************************************//
	
	@RequestMapping(value="/investorProfile", method = RequestMethod.GET)
	public String getInvestorProfile(@RequestParam("permalink") String permalink, ModelMap model) 
	{
		try
		{
			Investor investor = investorService.getInvestor(permalink);
			List<Company> companiesInvestedIn = companyService.getCompaniesGivenPermalinks(investor.getCompaniesInvestedIn());
			model.addAttribute("investor", investor);
			model.addAttribute("companies", companiesInvestedIn);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "investorProfile";
	}
	
	@RequestMapping(value="/companyProfile", method = RequestMethod.GET)
	public String getCompanyProfile(@RequestParam("permalink") String permalink, ModelMap model) 
	{
		try
		{
			Company company = companyService.getCompany(permalink);
			
			Map<String, List<String>> categorizedPermlinks = separateTypeOfInvestor(company.getInvestorPermalinks());
			List<Investor> personInvested = investorService.
					getInvestorsGivenPermalinks(categorizedPermlinks.get(CrunchbaseNamespace.PERSON.getLabel().toString()));
			List<Company> companyInvested = companyService.
					getCompaniesGivenPermalinks(categorizedPermlinks.get(CrunchbaseNamespace.COMPANY.getLabel().toString()));
			List<Financial_Org> finOrgInvested = financialOrgService.
					getFinancialOrgsGivenPermalinks(categorizedPermlinks.get(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString()));
			
			model.addAttribute("company", company);
			model.addAttribute("personInvested", personInvested);
			model.addAttribute("companyInvested", companyInvested);
			model.addAttribute("finOrgInvested", finOrgInvested);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "companyProfile";
	}
	
	@RequestMapping(value="/financialOrgProfile", method = RequestMethod.GET)
	public String getFinancialOrgProfile(@RequestParam("permalink") String permalink, ModelMap model) 
	{
		try
		{
			Financial_Org finOrg = financialOrgService.getFinancialOrg(permalink);
			List<Company> companiesInvestedIn = companyService.getCompaniesGivenPermalinks(finOrg.getCompaniesInvestedIn());
			
			model.addAttribute("finOrg", finOrg);
			model.addAttribute("companies", companiesInvestedIn);
		}
		catch(Exception ex)
	    {
	    	return "errorPage";
	    }
    	return "financialOrgProfilePage";
	}
	
	private Map<String, List<String>> separateTypeOfInvestor(Map<String, String> investorPermalinks)
	{
		Map<String, List<String>> categorizedPermlinks = new HashMap<String, List<String>>();
		if(investorPermalinks != null && !investorPermalinks.isEmpty())
		{
			for(Entry<String, String> pair : investorPermalinks.entrySet())
			{
				if(pair.getValue().equals(CrunchbaseNamespace.PERSON.getLabel().toString()))
				{
					if(!categorizedPermlinks.containsKey(CrunchbaseNamespace.PERSON.getLabel().toString()))
					{
						List<String> permalinks = new ArrayList<String>();
						permalinks.add(pair.getKey());
						categorizedPermlinks.put(CrunchbaseNamespace.PERSON.getLabel().toString(), permalinks);
					}
					else
					{
						categorizedPermlinks.get(CrunchbaseNamespace.PERSON.getLabel().toString()).add(pair.getKey());
					}
				}
				else if(pair.getValue().equals(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString()))
				{
					if(!categorizedPermlinks.containsKey(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString()))
					{
						List<String> permalinks = new ArrayList<String>();
						permalinks.add(pair.getKey());
						categorizedPermlinks.put(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString(), permalinks);
					}
					else
					{
						categorizedPermlinks.get(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString()).add(pair.getKey());
					}
				}
				else if(pair.getValue().equals(CrunchbaseNamespace.COMPANY.getLabel().toString()))
				{
					if(!categorizedPermlinks.containsKey(CrunchbaseNamespace.COMPANY.getLabel().toString()))
					{
						List<String> permalinks = new ArrayList<String>();
						permalinks.add(pair.getKey());
						categorizedPermlinks.put(CrunchbaseNamespace.COMPANY.getLabel().toString(), permalinks);
					}
					else
					{
						categorizedPermlinks.get(CrunchbaseNamespace.COMPANY.getLabel().toString()).add(pair.getKey());
					}
				}
			}
		}
		return categorizedPermlinks;
	}
	
	private void setCompanyPageParamenters(int page, List<Company> companies, ModelMap model)
	{
		companyResults = companies;
		int pageNumber = 1;
        pageNumber = page;
		int noOfRecords = companies.size();
    	int startIndex= (pageNumber-1)*recordsPerPage;
    	int endIndex = (noOfRecords - startIndex)>recordsPerPage ? (startIndex + recordsPerPage) : (startIndex + noOfRecords - startIndex);
    	List<Company> list = companies.subList(startIndex, endIndex);
    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
    	
    	model.addAttribute("noOfPages", noOfPages);
    	model.addAttribute("companies", list);
    	model.addAttribute("startIndex", startIndex+1);
    	model.addAttribute("endIndex", endIndex);
    	model.addAttribute("size", noOfRecords);
    	model.addAttribute("currentPage", page);
	}
	

	private void setInvestorPageParamenters(int page, List<Investor> investors, ModelMap model) 
	{
		investorResults = investors;
		int pageNumber = 1;
        pageNumber = page;
        int noOfRecords = investors.size();
    	int startIndex= (pageNumber-1)*recordsPerPage;
    	int endIndex = (noOfRecords - startIndex)>recordsPerPage ? (startIndex + recordsPerPage) : (startIndex + noOfRecords - startIndex);
    	List<Investor> list = investors.subList(startIndex, endIndex);
    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
    	
    	model.addAttribute("noOfPages", noOfPages);
    	model.addAttribute("investors", list);
    	model.addAttribute("startIndex", startIndex+1);
    	model.addAttribute("endIndex", endIndex);
    	model.addAttribute("size", noOfRecords);
    	model.addAttribute("currentPage", page);
	}
	
	private void setFinancialOrgPageParamenters(int page,List<Financial_Org> finOrgs, ModelMap model) 
	{
		financialOrgResults = finOrgs;
		int pageNumber = 1;
        pageNumber = page;
        int noOfRecords = finOrgs.size();
    	int startIndex= (pageNumber-1)*recordsPerPage;
    	int endIndex = (noOfRecords - startIndex)>recordsPerPage ? (startIndex + recordsPerPage) : (startIndex + noOfRecords - startIndex);
    	List<Financial_Org> list = finOrgs.subList(startIndex, endIndex);
    	int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
    	
    	model.addAttribute("noOfPages", noOfPages);
    	model.addAttribute("finOrgs", list);
    	model.addAttribute("startIndex", startIndex+1);
    	model.addAttribute("endIndex", endIndex);
    	model.addAttribute("size", noOfRecords);
    	model.addAttribute("currentPage", page);
	}
	
	private List<Company> getCompaniesToDisplay(boolean fundRanking, boolean followerRanking)
	{
		List<Company> allCompanies = companyService.getAllCompanies();
		if(!totalFundingVar.equalsIgnoreCase("1,2,3,4,5"))
		{
			allCompanies = companyFilterService.filterByFunds(totalFundingVar, allCompanies);
		}
		if(!comLocationVar.equalsIgnoreCase("1,2,3,4,5,6,7"))
		{
			allCompanies = companyFilterService.filterByLocation(comLocationVar, allCompanies);
		}
		if(!companyTypeVar.equalsIgnoreCase("1,2"))
		{
			allCompanies = companyFilterService.filterByType(companyTypeVar, allCompanies);
		}
		if(!followerRanking && !periodPastVar.equals("3"))
		{
			allCompanies = rankCompany.companyRankingByFundTime(periodPastVar, allCompanies);
		}
		if(!fundRanking && !comfollowersImpLevelVar.equals("3"))
		{
			allCompanies = rankCompany.getSortedCompanyBasedOnFC(comfollowersImpLevelVar, allCompanies);
		}
		return allCompanies;
	}
	
	private List<Investor> getInvestorsToDisplay()
	{
		List<Investor> investors = investorService.getAllInvestors();
		
		if(!investorLocationVar.equalsIgnoreCase("1,2,3,4,5,6,7"))
		{
			investors = investorFilterService.filterIndividualInvstorsByLocation(investorLocationVar, investors);
		}
		investors = rankedInvestor.getSortedInvestorBasedOnFC_CC_ROI(invFollowersImpLevelVar, invCompanyImpLevelVar, invRoiImpLevelVar, investors);
		return investors;
	}
	
	private List<Financial_Org> getFinOrgsToDisplay()
	{
		List<Financial_Org> finOrgs = financialOrgService.getAllFinancialOrgs();
		
		if(!finOrgLocationVar.equalsIgnoreCase("1,2,3,4,5,6,7"))
		{
			finOrgs = investorFilterService.filterInstitutionalInvstorsByLocation(finOrgLocationVar, finOrgs);
		}
		finOrgs = rankedInvestor.getSortedFinanciaOrgBasedOnFC_CC_ROI(finOrgFollowersImpLevelVar, 
				finOrgCompanyImpLevelVar, finOrgRoiImpLevelVar, finOrgs);
		return finOrgs;
	}
	
	private void setCompanyVariable(ModelMap model)
	{
		model.addAttribute("periods", periodPastVar);
    	model.addAttribute("comfollowersImpLevel", comfollowersImpLevelVar);
    	model.addAttribute("total_funding", totalFundingVar);
    	model.addAttribute("location", comLocationVar);
    	model.addAttribute("companyType", companyTypeVar);
	}
	
	private void setInvestorVariable(ModelMap model)
	{
		model.addAttribute("followerLevel", invFollowersImpLevelVar);
    	model.addAttribute("companyLevel", invCompanyImpLevelVar);
    	model.addAttribute("roiLevel", invRoiImpLevelVar);
    	model.addAttribute("location", investorLocationVar);
	}
	
	private void setFinOrgVariable(ModelMap model)
	{
		model.addAttribute("followerLevel", finOrgFollowersImpLevelVar);
    	model.addAttribute("companyLevel", finOrgCompanyImpLevelVar);
    	model.addAttribute("roiLevel", finOrgRoiImpLevelVar);
    	model.addAttribute("location", finOrgLocationVar);
	}
	
	private void setDefaultVariableValuesForCompany() 
	{
		periodPastVar = "3";
		comfollowersImpLevelVar = "3";
		totalFundingVar = "1,2,3,4,5";
		comLocationVar = "1,2,3,4,5,6,7";
		companyTypeVar = "1,2";
	}
	
	private void setDefaultVariableValuesForInvestor() 
	{
		invFollowersImpLevelVar = "3";
		invCompanyImpLevelVar = "3";
		invRoiImpLevelVar = "3";
		investorLocationVar = "1,2,3,4,5,6,7";
	}
	
	private void setDefaultVariableValuesForFinOrg() 
	{
		finOrgFollowersImpLevelVar = "3";
		finOrgCompanyImpLevelVar = "3";
		finOrgRoiImpLevelVar = "3";
		finOrgLocationVar = "1,2,3,4,5,6,7";
	}
}
