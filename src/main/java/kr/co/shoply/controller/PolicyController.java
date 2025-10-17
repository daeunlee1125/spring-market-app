package kr.co.shoply.controller;

import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.entity.Terms;
import kr.co.shoply.service.PolicyService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final SiteInfoService siteInfoService;

    @GetMapping("/policy/buyer")
    public String buyer(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "buyer");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "policy/buyer";
    }

    @GetMapping("/policy/seller")
    public String seller(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "seller");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "policy/seller";

    }

    @GetMapping("/policy/finance")
    public String finance(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "finance");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "policy/finance";
    }

    @GetMapping("/policy/privacy")
    public String privacy(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "privacy");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "policy/privacy";
    }

    @GetMapping("/policy/location")
    public String location(Model model) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "location");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "policy/location";
    }
}
