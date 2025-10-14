package kr.co.shoply.controller;

import kr.co.shoply.entity.Terms;
import kr.co.shoply.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/policy/buyer")
    public String buyer(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "buyer");
        return "policy/buyer";
    }

    @GetMapping("/policy/seller")
    public String seller(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "seller");
        return "policy/seller";
    }

    @GetMapping("/policy/finance")
    public String finance(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "finance");
        return "policy/finance";
    }

    @GetMapping("/policy/privacy")
    public String privacy(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "privacy");
        return "policy/privacy";
    }

    @GetMapping("/policy/location")
    public String location(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "location");
        return "policy/location";
    }
}
