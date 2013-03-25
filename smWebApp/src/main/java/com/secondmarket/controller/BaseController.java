package com.secondmarket.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.secondmarket.batch.CompanyService;
import com.secondmarket.batch.InvestorService;
import com.secondmarket.domain.Company;
import com.secondmarket.domain.Investor;

@Controller
@RequestMapping("/")
public class BaseController 
{
	// base controller
	protected static Logger logger = Logger.getLogger("controller");
	@Resource(name="investorService")
	private InvestorService investorService;
	@Resource(name="companyService")
	private CompanyService companyService;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String welcome(ModelMap model) {
 
		model.addAttribute("message", "Welcome!");
 
		//Spring uses InternalResourceViewResolver and return back index.jsp
		return "index";
 
	}
 
	@RequestMapping(value="/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {
 
		model.addAttribute("message", "Welcome " + name);
		return "index";
 
	}
	
	@RequestMapping(value="/companies", method = RequestMethod.GET)
	public String getCompanies(ModelMap model) {
 
		logger.debug("Received request to show all companies");
    	
    	// Retrieve all companies by delegating the call to CompanyService
    	List<Company> companies = companyService.getAll();
    	
    	// Attach Company to the Model
    	System.out.println(companies.size());
    	model.addAttribute("companies", companies);
    	
    	// This will resolve to /WEB-INF/jsp/companyPage.jsp
    	return "companyPage";
 
	}
	
	@RequestMapping(value="/investors", method = RequestMethod.GET)
	public String getInvestors(ModelMap model) {
 
		logger.debug("Received request to show all investors");
    	
    	// Retrieve all Investor by delegating the call to InvestorService
    	List<Investor> investors = investorService.getAll();
    	
    	// Attach Investor to the Model	
    	System.out.println(investors.size());
    	model.addAttribute("investors", investors);
    	
    	// This will resolve to /WEB-INF/jsp/investorsPage.jsp
    	return "investorsPage";
	}
	
	@RequestMapping(value="/investorsSearch", method = RequestMethod.GET)
	public String getSearchedInvestors(ModelMap model) {
 
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
