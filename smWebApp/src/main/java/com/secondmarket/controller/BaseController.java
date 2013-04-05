package com.secondmarket.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.secondmarket.batch.CompanyService;
import com.secondmarket.batch.InvestorService;
import com.secondmarket.batch.RankInvestor;
import com.secondmarket.domain.Company;
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
	@Resource(name="rankingService")
	private RankInvestor rankedInvestor;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String welcome(ModelMap model) 
	{
		model.addAttribute("message", "Welcome!");
		return "index"; //Spring uses InternalResourceViewResolver and return back index.jsp
	}
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String welcomeHome(ModelMap model) {
 
		model.addAttribute("message", "Welcome Home");
		return "index";
	}
	
	@RequestMapping(value="/companies", method = RequestMethod.GET)
	public String getCompanies(ModelMap model) 
	{
		logger.debug("Received request to show all companies");
    	// Retrieve all companies by delegating the call to CompanyService
    	List<Company> companies = companyService.getAll();
    	logger.debug(companies.size());
    	model.addAttribute("companies", companies);
    	return "companyPage";
	}
	
	@RequestMapping(value="/investors", method = RequestMethod.GET)
	public String getInvestors(ModelMap model) 
	{
		logger.debug("Received request to show all investors");	
    	// Retrieve all Investor by delegating the call to InvestorService
    	List<Investor> investors = investorService.getAll();
    	logger.debug(investors.size());
    	model.addAttribute("investors", investors);
    	model.addAttribute("followerLevel", "3");
    	model.addAttribute("companyLevel", "3");
    	return "investorsPage";
	}
	
	@RequestMapping(value="/investorRanking", method = RequestMethod.POST)
	public String getInvestorRanking(@RequestParam("followersImpLevel") String followersImpLevel, @RequestParam("companyImpLevel") String companyImpLevel, ModelMap model) 
	{
		logger.debug("Received request to rank investors, weight on followers, value = " + followersImpLevel);
		logger.debug("Received request to rank investors, weight on companies invested in, value = " + companyImpLevel);
    	// Retrieve all Investor by delegating the call to InvestorService
    	List<Investor> investors = rankedInvestor.getSortedInvestorBasedOnFC_CC(followersImpLevel, companyImpLevel);
    	logger.debug(investors.size());
    	model.addAttribute("investors", investors);
    	model.addAttribute("followerLevel", followersImpLevel);
    	model.addAttribute("companyLevel", companyImpLevel);
    	return "investorsPage";
	}
	
	@RequestMapping(value="/fundFilter",  method = RequestMethod.POST)
	public String filterCompanyByFund(@RequestParam("total_funding") String checkBoxVal, ModelMap model) 
	{
		logger.debug("Received request to filter company, by total funds raised, value = " + checkBoxVal);
		//System.out.println("total_funding:" + checkBoxVal);
		String[] parts = checkBoxVal.split(",");
		
    	// Retrieve all Investor by delegating the call to CompanyService
    	List<Company> companies = companyService.filterByFunds(parts);
    	model.addAttribute("companies", companies);
    	return "companyPage";
	}
	
	@RequestMapping(value="/investorProfile", method = RequestMethod.GET)
	public String getInvestorProfile(ModelMap model) {
 
		logger.debug("Received request to show investors detailed profile");
		model.addAttribute("investorsResults", "Results");
    	return "investorSearch";
	}
}
