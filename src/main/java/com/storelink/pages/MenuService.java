package com.storelink.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MenuService {

    public List<Menu> getMenus() {
        
        List<Menu> menus = new ArrayList<>();

        // Example menu: Dashboard
        Menu dashboards = new Menu();
        dashboards.setId(1);
        dashboards.setName("Dashboard");
        dashboards.setUrl("#");

        Menu dashboard1 = new Menu();
        dashboard1.setId(6);
        dashboard1.setName("Dashboard1");
        dashboard1.setUrl("/dashboard");

        dashboards.setSubMenus(List.of(dashboard1));


        // Example menu: Reports with submenus
        Menu reports = new Menu();
        reports.setId(2);
        reports.setName("Reports");
        reports.setUrl("/reports");

        Menu income = new Menu();
        income.setId(3);
        income.setName("Income");
        income.setUrl("/reports/income");

        Menu expenses = new Menu();
        expenses.setId(4);
        expenses.setName("Expenses");
        expenses.setUrl("/reports/expenses");

        reports.setSubMenus(List.of(income, expenses));

        // Add menus to the list
        menus.add(dashboards);
        menus.add(reports);

        return menus;
    }
}