package kr.co.shoply.controller.admin.cs;


import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.QnaDTO;
import kr.co.shoply.mapper.QnaMapper;
import kr.co.shoply.service.QnaService;
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

        return "admin/cs/qna/list";
    }

    @GetMapping("/admin/cs/qna/view")
    public String qnaView(){
        return "admin/cs/qna/view";
    }

    @ResponseBody // 페이지 리턴이 아닌 데이터(응답 상태)를 리턴하기 위함
    @PostMapping("/admin/cs/qna/deleteSelected")
    public ResponseEntity<?> deleteSelectedQna(@RequestBody List<Integer> qnaIds) {

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

}
