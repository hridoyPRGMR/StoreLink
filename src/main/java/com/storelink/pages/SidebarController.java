package com.storelink.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SidebarController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/")
    public String loadIndexPage(Model model) {
        model.addAttribute("menus", menuService.getMenus());
        return "index"; 
    }
}
