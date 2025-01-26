package com.storelink.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storelink.pages.MenuService;

@Controller
@RequestMapping("/cms")
public class SidebarController extends BaseController {

    public SidebarController(MenuService menuService) {
		super(menuService);
	}

    @GetMapping("/dashboard")
    public String loadIndexPage(Model model) {
    	System.out.println("Hello");
        return getPageContent(model,"dashboard");
    }
}
