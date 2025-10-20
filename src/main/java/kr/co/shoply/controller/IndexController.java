package kr.co.shoply.controller;

import kr.co.shoply.dto.*;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;
    private final BannerService bannerService;
    private final VersionService versionService;
    private final SiteInfoService siteInfoService;
    private final ProductService productService;

    @GetMapping({"/","/index"})
    public String index(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        model.addAttribute("newProducts", indexService.getNewProducts());
        model.addAttribute("hitProducts", indexService.getHitProducts());
        model.addAttribute("recommendedProducts", indexService.getRecommendedProducts());
        model.addAttribute("bestProducts", indexService.getBestProducts());
        model.addAttribute("discountProducts", indexService.getDiscountProducts());
        model.addAttribute("sidebarBestProducts", indexService.getSidebarBestProducts());
        model.addAttribute("banners", bannerService.mainBanners2());

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        return "index";
    }


}