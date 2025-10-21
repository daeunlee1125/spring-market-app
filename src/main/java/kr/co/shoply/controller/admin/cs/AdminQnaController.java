package kr.co.shoply.controller.admin.cs;


import kr.co.shoply.dto.*;
import kr.co.shoply.mapper.QnaMapper;
import kr.co.shoply.service.QnaService;
import kr.co.shoply.service.SiteInfoService;
import kr.co.shoply.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminQnaController {
    private final QnaService qnaService;
    private final VersionService versionService;
    private final SiteInfoService siteInfoService;

    @GetMapping("/admin/cs/qna/list")
    public String qnaList(
            @RequestParam(defaultValue = "1") int page, // 현재 페이지 (기본값 1)
            @RequestParam(required = false) String cate1,
            @RequestParam(required = false) String cate2,
            Model model) {

        // 서비스로부터 페이징 정보가 담긴 DTO를 받음
        PageResponseDTO<QnaDTO> pageResponse = qnaService.getQnaList3(page, cate1, cate2);

        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("cate1", cate1); // 필터 유지를 위해 다시 전달
        model.addAttribute("cate2", cate2); // 필터 유지를 위해 다시 전달
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);
        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/cs/qna/list";
    }

    @GetMapping("/admin/cs/qna/view")
    public String qnaView(@RequestParam("qNo") int qNo, Model model) {

        QnaDTO qnaDTO = qnaService.getQna3(qNo);
        if (qnaDTO == null) {
            // 데이터가 없으면 목록 페이지로 리다이렉트하고 메시지를 전달할 수 있습니다.
            return "redirect:/admin/cs/qna/list";
        }
        model.addAttribute("qnaDTO", qnaDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/cs/qna/view";
    }

    @GetMapping("/admin/cs/qna/reply")
    public String qnaReply(@RequestParam("qNo") int qNo, Model model) {

        QnaDTO qnaDTO = qnaService.getQna3(qNo);
        model.addAttribute("qnaDTO", qnaDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/cs/qna/reply";
    }

    @ResponseBody // 페이지 리턴이 아닌 데이터(응답 상태)를 리턴하기 위함
    @PostMapping("/admin/cs/qna/deleteSelected")
    public ResponseEntity<?> deleteSelectedQna(@RequestBody(required = false) List<Integer> qnaIds) {

        if (qnaIds == null || qnaIds.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
        }

        try {
            // 서비스에 List<Integer>를 전달하여 삭제 로직 수행
            qnaService.removeQnaList3(qnaIds);

            // 성공 시 200 OK 응답 반환
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // 에러 발생 시 500 Internal Server Error 응답 반환
            log.error("QNA 선택 삭제 중 에러 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @ResponseBody
    @PostMapping("/admin/cs/qna/modifyAnswerQna")
    public ResponseEntity<?> modifyAnswerQna(@RequestBody QnaAnswerRequestDTO requestDTO) {
        // 데이터 유효성 검사 (필요 시)
        if (requestDTO == null || requestDTO.getContent() == null || requestDTO.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("답변 내용이 비어있습니다.");
        }

        try {
            // 서비스 레이어를 호출하여 DB 업데이트 수행
            // (예시: qnaService에 qNo와 content를 넘겨 업데이트하는 메서드)
            qnaService.modifyAnswerQna3(requestDTO);

            // 성공 시 200 OK 응답과 함께 간단한 메시지 반환
            return ResponseEntity.ok().body("답변이 성공적으로 등록되었습니다.");

        } catch (Exception e) {
            log.error("QNA 답변 등록 중 에러 발생", e);
            // 실패 시 500 서버 에러 응답 반환
            return ResponseEntity.internalServerError().build();
        }
    }


}
