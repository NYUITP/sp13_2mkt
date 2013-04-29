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
		model.addAttribute("message", "Welcome!");
		return "index"; 
	}
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String welcomeHome(ModelMap model) {
 
		model.addAttribute("message", "Welcome Home");
		return "index";
	}
	
	//*************************************** Summary pages for Company, Investor and Fin Org ************************************************//
	
	@RequestMapping(value="/companies", method = RequestMethod.GET)
	public String getCompanies(ModelMap model) 
	{
		logger.debug("Received request to show all companies");
    	
		List<Company> companies = companyService.getAll();
    	logger.debug("Totat companies are - " + companies.size());
    	
    	model.addAttribute("companies", companies);
    	model.addAttribute("periods", "3");
    	return "companyPage";
	}
	
	@RequestMapping(value="/investors", method = RequestMethod.GET)
	public String getInvestors(ModelMap model) 
	{
		logger.debug("Received request to show all investors");	
    	
		List<Investor> investors = investorService.getAll();
    	logger.debug("Totat individual investors are - " + investors.size());
    	
    	model.addAttribute("investors", investors);
    	model.addAttribute("followerLevel", "3");
    	model.addAttribute("companyLevel", "3");
    	model.addAttribute("roiLevel", "3");
    	return "investorsPage";
	}
	
	@RequestMapping(value="/financialOrg", method = RequestMethod.GET)
	public String getFinancialOrg(ModelMap model) 
	{
		logger.debug("Received request to show all financial org");
    	
		List<Financial_Org> finOrgs = financialOrgService.getAll();
    	logger.debug("Totat financial orgs are - " + finOrgs.size());
    	
    	model.addAttribute("finOrgs", finOrgs);
    	return "financialOrgPage";
	}
	
	//*************************************************** Ranking Methods ********************************************************//
	
	@RequestMapping(value="/investorRankingByFC_CC_ROI", method = RequestMethod.POST)
	public String getInvestorRanking(@RequestParam("followersImpLevel") String followersImpLevel, 
			@RequestParam("companyImpLevel") String companyImpLevel, @RequestParam("roiImpLevel") String roiImpLevel, ModelMap model) 
	{
		logger.debug("Received request to rank investors, weight on followers, value = " + followersImpLevel);
		logger.debug("Received request to rank investors, weight on companies invested in, value = " + companyImpLevel);
		logger.debug("Received request to rank investors, weight on roi, value = " + roiImpLevel);
    	
		List<Investor> investors = rankedInvestor.getSortedInvestorBasedOnFC_CC_ROI(followersImpLevel, companyImpLevel, roiImpLevel);
    	logger.debug(investors.size());
    	
    	model.addAttribute("investors", investors);
    	model.addAttribute("followerLevel", followersImpLevel);
    	model.addAttribute("companyLevel", companyImpLevel);
    	model.addAttribute("roiLevel", roiImpLevel);
    	return "investorsPage";
	}
	
	@RequestMapping(value="/finOrgRankingByFC_CC_ROI", method = RequestMethod.POST)
	public String getFinancialOrgRanking(@RequestParam("followersImpLevel") String followersImpLevel, 
			@RequestParam("companyImpLevel") String companyImpLevel, @RequestParam("roiImpLevel") String roiImpLevel, ModelMap model) 
	{
		logger.debug("Received request to rank fin org, weight on followers, value = " + followersImpLevel);
		logger.debug("Received request to rank fin org, weight on companies invested in, value = " + companyImpLevel);
		logger.debug("Received request to rank fin org, weight on roi, value = " + roiImpLevel);
    	
		List<Financial_Org> financial_Orgs = rankedInvestor.getSortedFinanciaOrgBasedOnFC_CC_ROI(followersImpLevel, companyImpLevel, roiImpLevel);
    	logger.debug(financial_Orgs.size());
    	
    	model.addAttribute("finOrgs", financial_Orgs);
    	model.addAttribute("followerLevel", followersImpLevel);
    	model.addAttribute("companyLevel", companyImpLevel);
    	model.addAttribute("roiLevel", roiImpLevel);
    	return "financialOrgPage";
	}
	
	@RequestMapping(value="/companyRankingByFundTime", method = RequestMethod.POST)
	public String getCompanyRankingByFundTime(@RequestParam("periodPast") String periodPast, ModelMap model) 
	{
		logger.debug("Received request to rank companies, by fund time, value = " + periodPast);
    	
		List<Company> companies = rankCompany.companyRankingByFundTime(periodPast);
    	
    	model.addAttribute("companies", companies);
    	model.addAttribute("periods", periodPast);
    	return "companyPage";
	}
	
	@RequestMapping(value="/companyRankingByFollowers", method = RequestMethod.POST)
	public String getCompanyRankedByFollowers(@RequestParam("comfollowersImpLevel") String comfollowersImpLevel, ModelMap model) 
	{
		logger.debug("Received request to rank company, weight on followers, value = " + comfollowersImpLevel);
    	
		List<Company> companies = rankCompany.getSortedCompanyBasedOnFC(comfollowersImpLevel);
    	logger.debug(companies.size());
    	
    	model.addAttribute("companies", companies);
    	model.addAttribute("comfollowersImpLevel", comfollowersImpLevel);
    	return "companyPage";
	}
	
	//*************************************************** Filter Methods ***********************************************************//
	
	@RequestMapping(value="/companyFundFilter",  method = RequestMethod.POST)
	public String filterCompanyByFund(@RequestParam("total_funding") String checkBoxVal, ModelMap model) 
	{
		logger.debug("Received request to filter company, by total funds raised, value = " + checkBoxVal);
		
		String[] parts = checkBoxVal.split(",");
    	List<Company> companies = companyFilterService.filterByFunds(parts);
    	
    	model.addAttribute("companies", companies);
    	model.addAttribute("total_funding", checkBoxVal);
    	return "companyPage";
	}
		
	@RequestMapping(value="/starsFilter",  method = RequestMethod.POST)
	public String getInvestorByStar(@RequestParam("starLevel") String starLevel, ModelMap model) 
	{
		logger.debug("Received request to filter investor, by star, value = " + starLevel);
		List<Investor> investors = investorFilterService.filterByStar(starLevel);
    	
    	model.addAttribute("investors", investors);
    	model.addAttribute("starl", starLevel);
    	return "investorsPage";
	}
	
	@RequestMapping(value="/starsFilterFin",  method = RequestMethod.POST)
	public String getFinByStar(@RequestParam("starLevel") String starLevel, ModelMap model) 
	{
		logger.debug("Received request to filter institution investor, by star, value = " + starLevel);
		List<Financial_Org> financialOrg = investorFilterService.filterByStarFin(starLevel);
    	
    	model.addAttribute("finOrgs", financialOrg);
    	model.addAttribute("starl", starLevel);
    	return "financialOrgPage";
	}
	
	@RequestMapping(value="/companyLocationFilter",  method = RequestMethod.POST)
	public String filterCompanyByLocation(@RequestParam("location") String checkBoxVal, ModelMap model) 
	{
		
		logger.debug("Received request to filter company, by location, value = " + checkBoxVal);
		String[] parts = checkBoxVal.split(",");
    	List<Company> companies = companyFilterService.filterByLocation(parts);
    	
    	model.addAttribute("companies", companies);
    	return "companyPage";
	}
	
	@RequestMapping(value="/investorLocationFilter",  method = RequestMethod.POST)
	public String filterInvestorByLocation(@RequestParam("location") String checkBoxVal, ModelMap model) 
	{
		logger.debug("Received request to filter investors, by location, value = " + checkBoxVal);
		String[] parts = checkBoxVal.split(",");
    	List<Investor> investors = investorFilterService.filterByLocation(parts);
    	
    	model.addAttribute("investors", investors);
    	return "investorsPage";
	}
	
	//*************************************** Detailed pages for COmpany, Investor and Fin Org ************************************************//
	
	@RequestMapping(value="/investorProfile", method = RequestMethod.GET)
	public String getInvestorProfile(@RequestParam("permalink") String permalink, ModelMap model) 
	{
		logger.debug("Received request to show investors detailed profile");
		
		Investor investor = investorService.get(permalink);
		List<Company> companiesInvestedIn = companyService.get(investor.getCompaniesInvestedIn());
		
		model.addAttribute("investor", investor);
		model.addAttribute("companies", companiesInvestedIn);
    	return "investorProfile";
	}
	
	@RequestMapping(value="/companyProfile", method = RequestMethod.GET)
	public String getCompanyProfile(@RequestParam("permalink") String permalink, ModelMap model) 
	{
		logger.debug("Received request to show company detailed profile");
		Company company = companyService.get(permalink);
		
		Map<String, List<String>> categorizedPermlinks = separateTypeOfInvestor(company.getInvestorPermalinks());
		List<Investor> personInvested = investorService.get(categorizedPermlinks.get(CrunchbaseNamespace.PERSON.getLabel().toString()));
		List<Company> companyInvested = companyService.get(categorizedPermlinks.get(CrunchbaseNamespace.COMPANY.getLabel().toString()));
		List<Financial_Org> finOrgInvested = financialOrgService.get(categorizedPermlinks.get(CrunchbaseNamespace.FINANCIAL_ORG.getLabel().toString()));
		
		model.addAttribute("company", company);
		model.addAttribute("personInvested", personInvested);
		model.addAttribute("companyInvested", companyInvested);
		model.addAttribute("finOrgInvested", finOrgInvested);
    	return "companyProfile";
	}
	
	@RequestMapping(value="/financialOrgProfile", method = RequestMethod.GET)
	public String getFinancialOrgProfile(@RequestParam("permalink") String permalink, ModelMap model) 
	{
		logger.debug("Received request to show financial orgs detailed profile");
		
		Financial_Org finOrg = financialOrgService.get(permalink);
		List<Company> companiesInvestedIn = companyService.get(finOrg.getCompaniesInvestedIn());
		
		model.addAttribute("finOrg", finOrg);
		model.addAttribute("companies", companiesInvestedIn);
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
}
