package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.*;
import kr.co.shoply.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConfigController {
    private final SiteInfoService siteInfoService;
    private final TermsService termsService;
    private final VersionService versionService;
    private final Cate1Service cate1Service;
    private final Cate2Service cate2Service;
    private final BannerService bannerService;

    @GetMapping("/admin/config/banner")
    public String banner(Model model)
    {
        List<BannerDTO> banners = bannerService.getBanners2();
        model.addAttribute("banners", banners);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/config/banner";
    }

    @PostMapping("/admin/config/banner")
    public String banner(BannerDTO bannerDTO, @RequestParam("image") MultipartFile image) throws IOException {

        // 위치에 따라 폴더 지정
        String folder = switch (bannerDTO.getBan_location()) {
            case 1 -> "top";
            case 2 -> "slider";
            case 3 -> "detail";
            case 4 -> "login";
            case 5 -> "my";
            default -> throw new IllegalArgumentException("Invalid location");
        };

        String baseUploadPath = "/home/ec2-user/shoply/uploads/banner/" + folder + "/";
        Files.createDirectories(Paths.get(baseUploadPath));

        // 파일명 생성
        String uuid = UUID.randomUUID().toString();
        String originalName = image.getOriginalFilename();

        String safeName = StringUtils.cleanPath(originalName);
        safeName = safeName.replaceAll("[^a-zA-Z0-9._-]", "_");

        String newName = uuid + "_" + safeName;

        // 서버 저장
        Path filePath = Paths.get(baseUploadPath + newName);
        image.transferTo(filePath);

        // DB 경로 지정
        String dbPath = "/uploads/banner/" + folder + "/" + newName;
        bannerDTO.setBan_img(dbPath);

        bannerDTO.setBan_start_date(bannerDTO.getBan_start_ndate() + " " + bannerDTO.getBan_start_time());
        bannerDTO.setBan_end_date(bannerDTO.getBan_end_ndate() + " " + bannerDTO.getBan_end_time());

        // DB 저장
        bannerService.saveBanner(bannerDTO);

        return "redirect:/admin/config/banner";
    }

    @PostMapping("/admin/config/banner/delete")
    public String bannerDelete(@RequestParam("ban_no") List<Integer> ban_nos) {

        bannerService.deleteAllById(ban_nos);

        return "redirect:/admin/config/banner";
    }

    @PostMapping("/admin/config/banner/status")
    @ResponseBody
    public Map<String, Object> bannerStatus(@RequestBody Map<String, Object> req) {

        int ban_no = Integer.parseInt(req.get("ban_no").toString());
        int status = Integer.parseInt(req.get("status").toString());

        bannerService.changeBannerStatus(ban_no, status);

        return Map.of("success", true);
    }


    @GetMapping("/admin/config/basic")
    public String basic(Model model)
    {
        VersionDTO vdto = versionService.getVersion();
        System.out.println("versionDTO = " + vdto);
        model.addAttribute("version", vdto);
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo(1);
        model.addAttribute("siteInfo", siteInfoDTO);

        SiteInfoDTO getTitle = siteInfoService.getSiteInfo3();
        model.addAttribute("title", getTitle);



        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/config/basic";
    }

    @PostMapping("/admin/config/basic")
    public String basic(SiteInfoDTO siteInfoDTO, @RequestParam int type, @RequestParam(value = "img1", required = false) MultipartFile img1, @RequestParam(value = "img2", required = false) MultipartFile img2, @RequestParam(value = "img3", required = false) MultipartFile img3) throws IOException {

        siteInfoService.modifySiteInfo(siteInfoDTO, type, img1, img2, img3);

        return "redirect:/admin/config/basic";
    }
    @GetMapping("/admin/config/category")
    public String category(Model model)
    {
        List<Cate1DTO> cate1List = cate1Service.getCate1List();
        List<Cate2DTO> cate2List = cate2Service.getCate2List();

        model.addAttribute("cate1List", cate1List);
        model.addAttribute("cate2List", cate2List);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/config/category";
    }

    // 카테고리 삭제 시, cate2부터 다 삭제하고, 그 다음에 cate1 삭제해야 함
    // cate1 삭제할 때 하위 cate2도 자동으로 삭제시키려면 entity에서 외래키 매핑을 해줘야 하는데
    // product 쪽에서 짠 코드랑 어케저케 꼬일까봐....
    @PostMapping("/admin/config/category")
    public String category(@ModelAttribute CateformDTO cateformDTO){

        java.util.List<Cate1DTO> newCate1 = cate1Service.syncCate1(cateformDTO.getMainCategories());
        cateformDTO.setMainCategories(newCate1);


        cate2Service.syncCate2(cateformDTO.getSubCategories());

        return "redirect:/admin/config/category";
    }

    @GetMapping("/admin/config/policy")
    public String policy(Model model)
    {
        TermsDTO tdto = termsService.getTerms2(4);
        model.addAttribute("terms", tdto);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/config/policy";
    }

    @PostMapping("/admin/config/policy")
    public String policy(TermsDTO tdto){
        tdto.setT_no(4);
        termsService.updateTerms2(tdto);
        return "redirect:/admin/config/policy";
    }

    @GetMapping("admin/config/version")
    public String version(Model model)
    {
        List<VersionDTO> versions = versionService.getVersions();
        model.addAttribute("versions", versions);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/config/version";
    }

    @PostMapping("admin/config/version")
    public String version(VersionDTO versionDTO){
        versionService.saveVersion(versionDTO);
        return "redirect:/admin/config/version";
    }


}
