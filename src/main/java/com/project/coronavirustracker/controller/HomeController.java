package com.project.coronavirustracker.controller;

import com.project.coronavirustracker.model.LocationStats;
import com.project.coronavirustracker.services.CoronavirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronavirusDataService coronavirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> confirmedAllStats = coronavirusDataService.getConfirmedAllStats();
        int totalReportedCases = confirmedAllStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = confirmedAllStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();

        model.addAttribute("locationStats",confirmedAllStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    }

    @GetMapping("/total-confirmed-cases.html")
    public String totalConfirmedCases(Model model) {
        List<LocationStats> confirmedAllStats = coronavirusDataService.getConfirmedAllStats();
        int totalReportedCases = confirmedAllStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = confirmedAllStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();

        model.addAttribute("locationStats",confirmedAllStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "total-confirmed-cases";
    }

    @GetMapping("/total-recovered-cases.html")
    public String totalRecoveredCases(Model model) {
        List<LocationStats> recoveredAllStats = coronavirusDataService.getRecoveredAllStats();
        int totalRecoveredCases = recoveredAllStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewRecoveredCases = recoveredAllStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();

        model.addAttribute("locationStats", recoveredAllStats);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);
        model.addAttribute("totalNewRecoveredCases", totalNewRecoveredCases);
        return "total-recovered-cases";
    }

}
