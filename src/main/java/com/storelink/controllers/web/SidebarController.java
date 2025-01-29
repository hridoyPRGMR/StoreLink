package com.storelink.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cms")
public class SidebarController extends BaseController {


    @GetMapping("/dashboard")
    public String loadIndexPage(Model model) {
    	System.out.println("Hello");
        return getPageContent(model,"dashboard");
    }
}
