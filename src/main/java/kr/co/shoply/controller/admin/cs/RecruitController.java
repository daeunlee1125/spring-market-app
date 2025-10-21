package kr.co.shoply.controller.admin.cs;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.RecruitDTO;
import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.service.RecruitService;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecruitController {

    private final RecruitService recruitService;
    private final SiteInfoService siteInfoService;

    /** 목록 + 검색 + 페이지네이션 (기간 없음) */
    @GetMapping("/admin/cs/recruit/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<RecruitDTO> response = recruitService.list(pageRequestDTO);
        model.addAttribute("response", response);
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);
        return "/admin/cs/recruit/list";
    }

    /** 등록 (모달 POST) */
    @PostMapping("/admin/cs/recruit/register")
    public String registerPost(RecruitDTO dto) {
        recruitService.register(dto);
        return "redirect:/admin/cs/recruit/list?pg=1";
    }

    /** 상세 */
    @GetMapping("/admin/cs/recruit/read")
    public String read(@RequestParam Integer recNo,
                       @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                       Model model) {
        model.addAttribute("dto", recruitService.read(recNo));
        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);
        return "/admin/cs/recruit/read";
    }

    /** 수정 */
    @PostMapping("/admin/cs/recruit/modify")
    public String modify(RecruitDTO dto,
                         @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO) {
        recruitService.modify(dto);
        return "redirect:/admin/cs/recruit/read?recNo=" + dto.getRec_no();
    }

    /** 선택삭제: ids="1,2,3" */
    @PostMapping("/admin/cs/recruit/remove")
    public String remove(@RequestParam String ids,
                         @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO) {
        List<Integer> idList = Arrays.stream(ids.split(",")).map(Integer::parseInt).toList();
        recruitService.removeBulk(idList);
        return "redirect:/admin/cs/recruit/list";
    }
}
