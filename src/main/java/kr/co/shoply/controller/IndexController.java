package kr.co.shoply.controller;

import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.service.BannerService;
import kr.co.shoply.service.IndexService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;
    private final BannerService bannerService;

    @GetMapping({"/","/index"})
    public String index(Model model) {
        model.addAttribute("newProducts", indexService.getNewProducts());
        model.addAttribute("hitProducts", indexService.getHitProducts());
        model.addAttribute("recommendedProducts", indexService.getRecommendedProducts());
        model.addAttribute("bestProducts", indexService.getBestProducts());
        model.addAttribute("discountProducts", indexService.getDiscountProducts());
        model.addAttribute("sidebarBestProducts", indexService.getSidebarBestProducts());
        model.addAttribute("banners", bannerService.mainBanners2());
        return "index";
    }


}