package com.storelink.controllers.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.storelink.pages.MenuService;

public abstract class BaseController {
	
	private MenuService menuService;

	public BaseController(MenuService menuService) {
		 this.menuService=menuService;
	}

	@ModelAttribute
	public void addMenusToModel(Model model) {
		model.addAttribute("menus",menuService.getMenus());
	}
	
	public String getPageContent(Model model,String page) {
		model.addAttribute("pageContent",page);
		return "layout";
	}
	
}
