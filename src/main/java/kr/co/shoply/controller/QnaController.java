package kr.co.shoply.controller;

import kr.co.shoply.dto.QnaDTO;
import kr.co.shoply.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /* LIST */
    @GetMapping("/cs/qna/list")
    public String list(@RequestParam(required = false) String cat1,
                       @RequestParam(required = false) String cat2,
                       Model model){

        if ("전체".equals(cat1)) cat1 = null;
        if ("전체".equals(cat2)) cat2 = null;

        // 1차만 선택 시 → 첫 번째 2차로 리다이렉트
        if (cat1 != null && (cat2 == null || cat2.isBlank())) {
            List<String> c2 = qnaService.getCat2Master(cat1); // ← 여기!
            if (c2 != null && !c2.isEmpty()){
                String e1 = UriUtils.encode(cat1, StandardCharsets.UTF_8);
                String e2 = UriUtils.encode(c2.get(0), StandardCharsets.UTF_8);
                return "redirect:/cs/qna/list?cat1=" + e1 + "&cat2=" + e2;
            }
        }
        model.addAttribute("qnaList", qnaService.getQnaList(cat1, cat2));
        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", cat2);

        List<String> cat1List = qnaService.getCat1Master();          // ← 여기!
        List<String> cat2List = qnaService.getCat2Master(cat1);
        if (cat1List == null) cat1List = Collections.emptyList();
        if (cat2List == null) cat2List = Collections.emptyList();

        model.addAttribute("cat1List", cat1List);   // sidebar용
        model.addAttribute("cat2List", cat2List);   // sidebar용

        return "cs/qna/list";
    }

    /* VIEW */
    @GetMapping("/cs/qna/view")
    public String view(@RequestParam("qNo") int qNo, Model model){
        QnaDTO dto = qnaService.getQna(qNo);
        model.addAttribute("qnaDTO", dto);

        String cat1 = dto != null ? dto.getQCate1() : null;
        String cat2 = dto != null ? dto.getQCate2() : null;

        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", cat2);

        List<String> cat1List = qnaService.getCat1Master();          // ← 여기!
        List<String> cat2List = qnaService.getCat2Master(cat1);
        if (cat1List == null) cat1List = Collections.emptyList();
        if (cat2List == null) cat2List = Collections.emptyList();

        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);

        return "cs/qna/view";
    }

    /* WRITE FORM */
    @GetMapping("/cs/qna/write")
    public String registerForm(@RequestParam(required = false) String cat1,
                               @RequestParam(required = false) String cat2,
                               Model model){

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

        // sidebar용 (notice/faq 공용 sidebar 그대로 사용)
        List<String> cat1List = qnaService.getCat1Master();
        List<String> cat2List = qnaService.getCat2Master(cat1);
        if (cat1List == null) cat1List = Collections.emptyList();
        if (cat2List == null) cat2List = Collections.emptyList();
        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);

        return "cs/qna/write";
    }

    /* AJAX: 1차 선택 시 2차 목록 */
    @GetMapping("/cs/qna/cat2")
    @ResponseBody
    public List<String> qnaCat2(@RequestParam String cat1){
        List<String> cat2 = qnaService.getCat2Master(cat1);
        if (cat2 == null) cat2 = Collections.emptyList();
        return cat2;
    }

    /* WRITE SUBMIT */
    @PostMapping("/cs/qna/write")
    public String registerSubmit(@ModelAttribute("qnaForm") QnaDTO form){
        int saved = qnaService.register(form);
        String e1 = form.getQCate1() == null ? "" : UriUtils.encode(form.getQCate1(), StandardCharsets.UTF_8);
        String e2 = form.getQCate2() == null ? "" : UriUtils.encode(form.getQCate2(), StandardCharsets.UTF_8);
        return "redirect:/cs/qna/list?cat1=" + e1 + "&cat2=" + e2;
    }
}
