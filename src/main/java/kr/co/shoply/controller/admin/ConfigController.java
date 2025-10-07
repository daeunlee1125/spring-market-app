package kr.co.shoply.controller.admin;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.co.shoply.dto.*;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConfigController {
    private final SiteInfoService siteInfoService;
    private final TermsService termsService;
    private final VersionService  versionService;
    private final Cate1Service cate1Service;
    private final Cate2Service cate2Service;
    private final EntityManager entityManager;

    @GetMapping("/admin/config/banner")
    public String banner()
    {
        return "admin/config/banner";
    }
    @GetMapping("/admin/config/basic")
    public String basic(Model model)
    {
        VersionDTO vdto = versionService.getVersion();
        System.out.println("versionDTO = " + vdto);
        model.addAttribute("version", vdto);
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo(1);
        model.addAttribute("siteInfo", siteInfoDTO);
        return "admin/config/basic";
    }
    @PostMapping("/admin/config/basic")
    public String basic(SiteInfoDTO siteInfoDTO, @RequestParam int type){

        siteInfoService.modifySiteInfo(siteInfoDTO, type);

        return "redirect:/admin/config/basic";
    }
    @GetMapping("/admin/config/category")
    public String category(Model model)
    {
        List<Cate1DTO> cate1List = cate1Service.getCate1List();
        List<Cate2DTO> cate2List = cate2Service.getCate2List();

        model.addAttribute("cate1List", cate1List);
        model.addAttribute("cate2List", cate2List);

        return "admin/config/category";
    }

    // 카테고리 삭제 시, cate2부터 다 삭제하고, 그 다음에 cate1 삭제해야 함
    // cate1 삭제할 때 하위 cate2도 자동으로 삭제시키려면 entity에서 외래키 매핑을 해줘야 하는데
    // product 쪽에서 짠 코드랑 어케저케 꼬일까봐....
    @PostMapping("/admin/config/category")
    public String category(@ModelAttribute CateformDTO cateformDTO){

        cate1Service.syncCate1(cateformDTO.getMainCategories());

        cate2Service.syncCate2(cateformDTO.getSubCategories());

        return "redirect:/admin/config/category";
    }

    @GetMapping("/admin/config/policy")
    public String policy(Model model)
    {
        TermsDTO tdto = termsService.getTerms2(1);
        model.addAttribute("terms", tdto);
        return "admin/config/policy";
    }

    @PostMapping("/admin/config/policy")
    public String policy(TermsDTO tdto){
        tdto.setT_no(1);
        termsService.updateTerms2(tdto);
        return "redirect:/admin/config/policy";
    }

    @GetMapping("admin/config/version")
    public String version(Model model)
    {
        List<VersionDTO> versions = versionService.getVersions();
        model.addAttribute("versions", versions);
        return "admin/config/version";
    }

    @PostMapping("admin/config/version")
    public String version(VersionDTO versionDTO){
        versionService.saveVersion(versionDTO);
        return "redirect:/admin/config/version";
    }


}
