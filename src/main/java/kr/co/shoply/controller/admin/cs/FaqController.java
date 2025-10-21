package kr.co.shoply.controller.admin.cs;


import kr.co.shoply.dto.CopyrightDTO;
import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.service.CsFaqService;
import kr.co.shoply.service.SiteInfoService;
import kr.co.shoply.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FaqController {


    private final CsFaqService csFaqService;
    private final VersionService versionService;
    private final SiteInfoService siteInfoService;


    // FAQ 목록
    @GetMapping("/admin/cs/faq/list")
    public String list(
            @RequestParam(value = "cate1", required = false) String cate1,
            @RequestParam(value = "cate2", required = false) String cate2,
            Model model
    ) {
        List<CsFaqDTO> faqList = csFaqService.selectFaqList(cate1, cate2);
        model.addAttribute("faqList", faqList);
        model.addAttribute("cate1", cate1);
        model.addAttribute("cate2", cate2);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/cs/faq/list";
    }



    // 단일 삭제 ([삭제] 클릭 시)
    @GetMapping("/admin/cs/faq/delete/{no}")
    public String deleteFaq(@PathVariable("no") int cs_faq_no) {
        csFaqService.deleteFaq(cs_faq_no);
        return "redirect:/admin/cs/faq/list";
    }

    // 다중 삭제 (선택삭제 버튼 클릭 시)
    @PostMapping("/admin/cs/faq/delete")
    public String deleteSelected(@RequestParam("deleteIds") String deleteIds) {
        List<Integer> ids = Arrays.stream(deleteIds.split(","))
                .map(Integer::parseInt)
                .toList();

        csFaqService.deleteFaqs(ids);
        return "redirect:/admin/cs/faq/list";
    }


    // 수정 폼 열기
    @GetMapping("/admin/cs/faq/modify/{no}")
    public String modifyForm(@PathVariable("no") int cs_faq_no, Model model) {
        CsFaqDTO faq = csFaqService.findById(cs_faq_no);
        model.addAttribute("faq", faq);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "admin/cs/faq/modify";
    }

    // 수정 처리
    @PostMapping("/admin/cs/faq/modify")
    public String modifySubmit(CsFaqDTO faq) {
        csFaqService.updateFaq(faq);
        return "redirect:/admin/cs/faq/list";
    }



    // 상세보기 (글 눌렀을 때)
    @GetMapping("/admin/cs/faq/view/{no}")
    public String viewFaq(@PathVariable("no") int cs_faq_no, Model model) {

        // 글 번호로 FAQ 조회
        CsFaqDTO faq = csFaqService.findById(cs_faq_no);

        // 조회수 증가 로직 (선택사항)
        csFaqService.increaseHit(cs_faq_no);

        // 모델에 FAQ 데이터 담기
        model.addAttribute("faq", faq);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        // view.html 반환
        return "admin/cs/faq/view";
    }


    // FAQ 글쓰기 폼
    @GetMapping("/admin/cs/faq/write")
    public String writeForm(Model model) {
        model.addAttribute("faq", new CsFaqDTO());

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "admin/cs/faq/write";
    }

    // FAQ 글 등록 처리
    @PostMapping("/admin/cs/faq/write")
    public String writeFaq(@ModelAttribute CsFaqDTO faqDTO) {
        csFaqService.saveFaq(faqDTO);
        return "redirect:/admin/cs/faq/list";
    }


}
