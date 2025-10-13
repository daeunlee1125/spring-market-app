package kr.co.shoply.controller.admin.cs;


import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.CsNoticeService;
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
public class NoticeController {

    private final CsNoticeService csNoticeService;

    @GetMapping("/admin/cs/notice/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {

        String csType = pageRequestDTO.getCsType();

        if (csType == null || csType.trim().isEmpty() || csType.equals("전체")) {
            pageRequestDTO.setCsType(null);
        }

        PageResponseDTO<CsNoticeDTO> pageResponse = csNoticeService.selectNoticeList(pageRequestDTO);
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("csType", csType);

        return "/admin/cs/notice/list";
    }

    // 글 삭제(체크박스)
    @PostMapping("/admin/cs/notice/delete")
    public String deleteNotices(@RequestParam("deleteIds") String deleteIds) {
        List<Integer> ids = Arrays.stream(deleteIds.split(","))
                .map(Integer::parseInt)
                .toList();

        csNoticeService.deleteNotices(ids);

        return "redirect:/admin/cs/notice/list";
    }

    // 단일 글 삭제 ([삭제] 클릭 시)
    @GetMapping("/admin/cs/notice/delete/{csNo}")
    public String deleteNotice(@PathVariable("csNo") int csNo) {
        csNoticeService.deleteNotices(List.of(csNo)); // 기존 다중 삭제 재활용!
        return "redirect:/admin/cs/notice/list";
    }



    // 글 작성
    @GetMapping("/admin/cs/notice/write")
    public String writeForm(Model model) {
        model.addAttribute("notice", new CsNoticeDTO());
        return "admin/cs/notice/write";
    }

    @PostMapping("/admin/cs/notice/write")
    public String write(@ModelAttribute CsNoticeDTO noticeDTO) {
        csNoticeService.insertNotice(noticeDTO);
        return "redirect:/admin/cs/notice/list";
    }

    // 수정 페이지 진입
    @GetMapping("/admin/cs/notice/modify/{csNo}")
    public String modifyForm(@PathVariable("csNo") int csNo, Model model) {
        CsNoticeDTO notice = csNoticeService.getNotice(csNo);
        model.addAttribute("notice", notice);
        return "admin/cs/notice/modify";
    }

    // 수정 처리
    @PostMapping("/admin/cs/notice/modify")
    public String modifySubmit(CsNoticeDTO dto) {
        csNoticeService.updateNotice(dto);
        return "redirect:/admin/cs/notice/list";
    }



    // 글 보기
    @GetMapping("/admin/cs/notice/view/{csNo}")
    public String viewNotice(@PathVariable("csNo") int csNo, Model model) {
        // 조회수 증가
        csNoticeService.incrementHit(csNo);

        // 글 정보 불러오기
        CsNoticeDTO notice = csNoticeService.getNotice(csNo);
        model.addAttribute("notice", notice);

        return "admin/cs/notice/view";
    }



}






