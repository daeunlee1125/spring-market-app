package kr.co.shoply.controller;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.service.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    private final CsService csService;

    @GetMapping("/cs")
    public String csIndex(){
        return "cs/index";
    }

    @GetMapping("/cs/notice/list")
    public String list(Model model){
        List<CsNoticeDTO> noticeList = csService.getCsNoticeAll();
        model.addAttribute("noticeList", noticeList);
        return "cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String view(@RequestParam("cs_no") int cs_no, Model model){
        log.info("cs_no:{}",cs_no);

        CsNoticeDTO csNoticeDTO = csService.getCsNotice(cs_no);
        model.addAttribute("csNoticeDTO", csNoticeDTO);
        return "cs/notice/view";
    }

    @GetMapping("/cs/faq/list")
    public String flist(Model model){
        List<CsFaqDTO> csqList = csService.getCsFaqAll();
        model.addAttribute("csqList", csqList);
        return "cs/faq/list";
    }

    @GetMapping("/cs/faq/view")
    public String fview(@RequestParam("cs_faq_no") int cs_faq_no, Model model){
        log.info("cs_faq_no:{}",cs_faq_no);

        CsFaqDTO csFaqDTO = csService.getCsFaq(cs_faq_no);
        model.addAttribute("csFaqDTO", csFaqDTO);
        return "cs/faq/view";
    }

}
