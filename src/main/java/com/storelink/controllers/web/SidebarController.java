package com.storelink.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storelink.pages.MenuService;

@Controller
@RequestMapping("/cms")
public class SidebarController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/dashboard")
    public String loadIndexPage(Model model) {
        model.addAttribute("menus", menuService.getMenus());       
        return "index"; 
    }
}
