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
    	return "investorsPage";
	}
	
	@RequestMapping(value="/investorRanking", method = RequestMethod.POST)
	public String getInvestorRanking(@RequestParam("followersImpLevel") String followersImpLevel, ModelMap model) 
	{
		logger.debug("Received request to rank investors, value = " + followersImpLevel);
    	// Retrieve all Investor by delegating the call to InvestorService
    	List<Investor> investors = rankedInvestor.getSortedInvestorBasedOnFC_CC(followersImpLevel);
    	logger.debug(investors.size());
    	model.addAttribute("investors", investors);
    	return "investorsPage";
	}
	
	@RequestMapping(value="/investorsSearch", method = RequestMethod.GET)
	public String getSearchedInvestors(ModelMap model)
	{
		logger.debug("Received request to show investors");
    	return "investorSearch";
	}
	
	@RequestMapping(value="/investorSearchResults", method = RequestMethod.GET)
	public String getInvestorResults(ModelMap model) {
 
		logger.debug("Received request to show investors");
		model.addAttribute("investorsResults", "Results");
    	return "investorSearch";
	}
}
