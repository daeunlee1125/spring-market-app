package kr.co.shoply.controller;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.QnaDTO;
import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.service.QnaService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private final SiteInfoService siteInfoService;

    /* -------- LIST -------- */
    @GetMapping("/cs/qna/list")
    public String list(@RequestParam(required = false) String cat1,
                       @RequestParam(required = false) String cat2,
                       @ModelAttribute PageRequestDTO pageRequestDTO,
                       Model model) {

        if ("전체".equals(cat1)) cat1 = null;
        if ("전체".equals(cat2)) cat2 = null;

        // 1차만 선택 시 첫 번째 2차로 리다이렉트
        if (cat1 != null && (cat2 == null || cat2.isBlank())) {
            List<String> c2 = qnaService.getCat2Master(cat1);
            if (c2 != null && !c2.isEmpty()) {
                String e1 = UriUtils.encode(cat1, StandardCharsets.UTF_8);
                String e2 = UriUtils.encode(c2.get(0), StandardCharsets.UTF_8);
                return "redirect:/cs/qna/list?cat1=" + e1 + "&cat2=" + e2;
            }
        }

        PageResponseDTO<QnaDTO> pageResponseDTO = qnaService.getQnaPage(pageRequestDTO, cat1, cat2);

        model.addAttribute("qnaList", pageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", cat2);

        // 사이드바용 마스터 카테고리
        List<String> cat1List = qnaService.getCat1Master();
        List<String> cat2List = qnaService.getCat2Master(cat1);
        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "cs/qna/list";
    }

    /* -------- VIEW -------- */
    @GetMapping("/cs/qna/view")
    public String view(@RequestParam(value = "qNo", required = false) Integer qNoParam,
                       @RequestParam(value = "q_no", required = false) Integer qNoSnake,
                       Model model) {

        // qNo 또는 q_no 둘 중 들어온 값 사용
        Integer qNo = qNoParam != null ? qNoParam : qNoSnake;
        if (qNo == null) {
            return "redirect:/cs/qna/list";
        }

        QnaDTO dto = qnaService.getQna(qNo);
        model.addAttribute("qnaDTO", dto);

        String cat1 = dto != null ? dto.getQ_cate1() : null;
        String cat2 = dto != null ? dto.getQ_cate2() : null;

        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", cat2);

        // 사이드바: 마스터 카테고리 사용
        List<String> cat1List = qnaService.getCat1Master();
        List<String> cat2List = qnaService.getCat2Master(cat1);
        if (cat1List == null) cat1List = Collections.emptyList();
        if (cat2List == null) cat2List = Collections.emptyList();

        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "cs/qna/view";
    }

    /* -------- WRITE FORM -------- */
    @GetMapping("/cs/qna/write")
    public String registerForm(@RequestParam(required = false) String cat1,
                               @RequestParam(required = false) String cat2,
                               Model model) {

        model.addAttribute("qnaForm", new QnaDTO());
        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", cat2);

        // 폼용 마스터
        List<String> cat1Master = qnaService.getCat1Master();
        List<String> cat2Master = qnaService.getCat2Master(cat1);
        if (cat1Master == null) cat1Master = Collections.emptyList();
        if (cat2Master == null) cat2Master = Collections.emptyList();
        model.addAttribute("cat1Master", cat1Master);
        model.addAttribute("cat2Master", cat2Master);

        // 사이드바용 (마스터)
        List<String> cat1List = qnaService.getCat1Master();
        List<String> cat2List = qnaService.getCat2Master(cat1);
        if (cat1List == null) cat1List = Collections.emptyList();
        if (cat2List == null) cat2List = Collections.emptyList();
        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        return "cs/qna/write";
    }

    /* -------- AJAX: 1차 선택 시 2차 목록 -------- */
    @GetMapping("/cs/qna/cat2")
    @ResponseBody
    public List<String> qnaCat2(@RequestParam String cat1) {
        List<String> cat2 = qnaService.getCat2Master(cat1);
        return (cat2 != null) ? cat2 : Collections.emptyList();
    }

    /* -------- WRITE SUBMIT -------- */
    @PostMapping("/cs/qna/write")
    public String registerSubmit(@ModelAttribute("qnaForm") QnaDTO form,
                                 Authentication authentication) {

        // 비로그인 방어
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            String ret = UriUtils.encode("/cs/qna/write", StandardCharsets.UTF_8);
            return "redirect:/member/login?redirect=" + ret;
        }

        // ★ 여기서 확실히 mem_id 주입
        form.setMem_id(authentication.getName());

        int saved = qnaService.register(form);

        String e1 = form.getQ_cate1() == null ? "" : UriUtils.encode(form.getQ_cate1(), StandardCharsets.UTF_8);
        String e2 = form.getQ_cate2() == null ? "" : UriUtils.encode(form.getQ_cate2(), StandardCharsets.UTF_8);
        return "redirect:/cs/qna/list?cat1=" + e1 + "&cat2=" + e2;
    }
}
