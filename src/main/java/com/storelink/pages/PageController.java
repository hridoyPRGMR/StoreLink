package com.storelink.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	

	
	@GetMapping("/dashboard")
    public String loadDashboard() {
        return "fragments/dashboard"; // Fragment for dashboard content
    }

    @GetMapping("/reports/income")
    public String loadIncomeReports() {
        return "fragments/income"; // Fragment for income reports
    }

    @GetMapping("/reports/expenses")
    public String loadExpenseReports() {
        return "fragments/expenses"; // Fragment for expense reports
    }
	
}

