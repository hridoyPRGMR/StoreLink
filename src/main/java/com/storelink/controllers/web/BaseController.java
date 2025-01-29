package com.storelink.controllers.web;

import org.springframework.ui.Model;


public abstract class BaseController {
	
	
	public String getPageContent(Model model,String page) {
		model.addAttribute("pageContent",page);
		return "layout";
	}
	
}
