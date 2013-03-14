package com.secondmarket.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.secondmarket.domain.Investor;
import com.secondmarket.service.InvestorService;

@Controller
@RequestMapping("/")
public class BaseController 
{
	// base controller
	protected static Logger logger = Logger.getLogger("controller");
	@Resource(name="investorService")
	private InvestorService investorService;
	
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
 
		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - welcome()");
		
		//Spring uses InternalResourceViewResolver and return back index.jsp
		return "companies";
 
	}
	
	@RequestMapping(value="/investors", method = RequestMethod.GET)
	public String getInvestors(ModelMap model) {
 
		logger.debug("Received request to show all investors");
    	
    	// Retrieve all Investor by delegating the call to InvestorService
    	List<Investor> investors = investorService.getAll();
    	
    	// Attach Investor to the Model
    	System.out.println(investors.size());
    	model.addAttribute("investors", investors);
    	
    	// This will resolve to /WEB-INF/jsp/investors.jsp
    	return "investorsPage";
	}
 
}
