package kr.co.shoply.controller;

import kr.co.shoply.service.IndexService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newProducts", indexService.getNewProducts());
        model.addAttribute("hitProducts", indexService.getHitProducts());
        model.addAttribute("recommendedProducts", indexService.getRecommendedProducts());
        model.addAttribute("bestProducts", indexService.getBestProducts());
        model.addAttribute("discountProducts", indexService.getDiscountProducts());
        return "index";
    }
}