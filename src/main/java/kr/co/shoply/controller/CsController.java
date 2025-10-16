package kr.co.shoply.controller;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.CsNoticeService;
import kr.co.shoply.service.CsService;
import kr.co.shoply.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    private final CsService csService;
    private final QnaService qnaService;

    @GetMapping("/cs")
    public String csIndex(Model model) {
        // 공지사항 5개
        model.addAttribute("noticeList", csService.getRecentNotices(5));
        model.addAttribute("qnaList", qnaService.getRecentQna(5));
        return "cs/index";
    }
    @GetMapping("/cs/notice/list")
    public String noticeList(@RequestParam(required = false) String cat1,
                             @ModelAttribute PageRequestDTO pageRequestDTO,
                             Model model) {

        if ("전체".equals(cat1)) cat1 = null;

        // 분류(cat1)를 PageRequestDTO의 csType에 실어 서비스로 전달
        pageRequestDTO.setCsType(cat1);
        // 페이지/사이즈 기본값은 PageRequestDTO @Builder.Default 값 사용

        PageResponseDTO<CsNoticeDTO> pageResponseDTO = csService.getCsNoticePage(pageRequestDTO);

        // 사이드바용 1차 목록(공지 고정 셋)
        List<String> cat1List = List.of("전체","고객서비스","안전거래","위해상품","이벤트당첨");

        model.addAttribute("noticeList", pageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        model.addAttribute("cat1List", cat1List);
        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", null);   // 공지는 2차 없음
        model.addAttribute("cat2List", List.of());

        return "cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String noticeView(@RequestParam("cs_no") int csNo, Model model) {
        var dto = csService.getCsNotice(csNo);
        if (dto == null) return "redirect:/cs/notice/list";

        model.addAttribute("csNoticeDTO", dto);

        // 뷰에서도 사이드바 유지
        model.addAttribute("selCat1", dto.getCs_type());
        model.addAttribute("selCat2", null);
        model.addAttribute("cat1List", List.of("전체","고객서비스","안전거래","위해상품","이벤트당첨"));
        model.addAttribute("cat2List", List.of());

        return "cs/notice/view";
    }

    /* ---------------- FAQ ---------------- */
    @GetMapping("/cs/faq/list")
    public String faqList(@RequestParam(required = false) String cat1,
                          @RequestParam(required = false) String cat2,
                          Model model) {

        // '전체'는 미선택과 동일 처리
        if ("전체".equals(cat1)) cat1 = null;
        if ("전체".equals(cat2)) cat2 = null;

        // cat1, cat2가 아예 없으면 기본값으로 이동 (배송 > 당일배송)
        if (cat1 == null && cat2 == null) {
            return "redirect:/cs/faq/list?cat1=" + UriUtils.encode("배송", StandardCharsets.UTF_8)
                    + "&cat2=" + UriUtils.encode("당일배송", StandardCharsets.UTF_8);
        }

        // 1차만 선택됐을 때 첫 번째 2차로 리다이렉트
        if (cat1 != null && (cat2 == null || cat2.isBlank())) {
            List<String> cat2ListForRedirect = csService.getCat2List(cat1);
            if (cat2ListForRedirect != null && !cat2ListForRedirect.isEmpty()) {
                String encodedCat1 = UriUtils.encode(cat1, StandardCharsets.UTF_8);
                String encodedCat2 = UriUtils.encode(cat2ListForRedirect.get(0), StandardCharsets.UTF_8);
                return "redirect:/cs/faq/list?cat1=" + encodedCat1 + "&cat2=" + encodedCat2;
            }
        }

        // 데이터 바인딩
        model.addAttribute("csqList", csService.getCsFaqList(cat1, cat2));
        model.addAttribute("selCat1", cat1);
        model.addAttribute("selCat2", cat2);
        model.addAttribute("cat1List", csService.getCat1List());
        model.addAttribute("cat2List", csService.getCat2List(cat1));

        return "cs/faq/list";
    }

    /* ---------------- FAQ 상세 ---------------- */
    @GetMapping("/cs/faq/view")
    public String faqView(@RequestParam("faqNo") int faqNo, Model model) {
        log.info("faqNo: {}", faqNo);

        CsFaqDTO dto = csService.getCsFaq(faqNo);
        if (dto == null) {
            return "redirect:/cs/faq/list";
        }

        // 상세 표시
        model.addAttribute("csFaqDTO", dto);

        // ✅ DTO가 스네이크 케이스이므로 접근자도 스네이크(getCs_faq_*) 사용
        String c1 = dto.getCs_faq_cate1();
        String c2 = dto.getCs_faq_cate2();

        // 사이드바 표시용
        model.addAttribute("selCat1", c1);
        model.addAttribute("selCat2", c2);
        model.addAttribute("cat1List", csService.getCat1List());
        model.addAttribute("cat2List", csService.getCat2List(c1));

        return "cs/faq/view";
    }

}
