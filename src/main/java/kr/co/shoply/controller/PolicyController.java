package kr.co.shoply.controller;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.entity.Terms;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.IndexService;
import kr.co.shoply.service.PolicyService;
import kr.co.shoply.service.ProductService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final SiteInfoService siteInfoService;
    private final ProductService productService;
    private final IndexService indexService;

    @GetMapping("/policy/buyer")
    public String buyer(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "buyer");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        return "policy/buyer";
    }

    @GetMapping("/policy/seller")
    public String seller(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "seller");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        return "policy/seller";

    }

    @GetMapping("/policy/finance")
    public String finance(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "finance");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        return "policy/finance";
    }

    @GetMapping("/policy/privacy")
    public String privacy(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "privacy");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        return "policy/privacy";
    }

    @GetMapping("/policy/location")
    public String location(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Terms terms = policyService.getTerms();
        model.addAttribute("terms", terms);
        model.addAttribute("activePage", "location");

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        List<Cate1DTO> cate1DTOList = productService.getCate1List();

        for (Cate1DTO cate1 : cate1DTOList) {
            // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
            List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

            // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
            cate1.setSubCategories(subList);
        }

        model.addAttribute("cate1DTOList", cate1DTOList);

        if(myUserDetails != null) {
            String memId = myUserDetails.getUsername();
            int cartCount = indexService.getCartCount3(memId);
            model.addAttribute("cartCount", cartCount);
        }

        return "policy/location";
    }
}
