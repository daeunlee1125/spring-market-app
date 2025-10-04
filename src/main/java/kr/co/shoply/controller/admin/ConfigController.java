package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.dto.TermsDTO;
import kr.co.shoply.dto.VersionDTO;
import kr.co.shoply.service.SiteInfoService;
import kr.co.shoply.service.TermsService;
import kr.co.shoply.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConfigController {
    private final SiteInfoService siteInfoService;
    private final TermsService termsService;
    private final VersionService  versionService;

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
    public String category()
    {
        return "admin/config/category";
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
