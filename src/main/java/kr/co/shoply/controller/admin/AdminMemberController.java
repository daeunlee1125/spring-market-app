package kr.co.shoply.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMemberController {

    private final AdminMemberService adminMemberService;


    @GetMapping("/admin/member/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<MemberDTO> pageResponseDTO = adminMemberService.selectMembers(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail")
    public String memberDetail(@RequestParam("mem_id") String memId,
                               @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                               Model model) {

        // 1. 선택한 회원 정보 조회
        MemberDTO selectedMember = adminMemberService.getMemberDetail(memId);

        // 2. 목록 다시 불러오기 (페이지 유지)
        PageResponseDTO<MemberDTO> pageResponseDTO = adminMemberService.selectMembers(pageRequestDTO);

        // 3. 모델에 추가
        model.addAttribute("selectedMember", selectedMember);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("openModal", true); // 모달 표시 플래그

        return "admin/member/list";
    }


    @PostMapping("/admin/member/update")
    public String AdminupdateMember(@ModelAttribute MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        adminMemberService.AdminupdateMember(memberDTO);
        redirectAttributes.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
        return "redirect:/admin/member/list";
    }

    @PostMapping("/admin/member/updateStatus")
    public String updateAdminMemberStatus(@RequestParam("mem_id") String memId,
                                          @RequestParam("mem_stat") String memStat,
                                          RedirectAttributes redirectAttributes) {

        adminMemberService.updateAdminMemberStatus(memId, memStat);
        redirectAttributes.addFlashAttribute("msg",
                "회원 상태가 '" + memStat + "'으로 변경되었습니다.");

        return "redirect:/admin/member/list";
    }


    @PostMapping("/admin/member/updateRank")
    public String AdminupdateRank(@RequestParam("jsonPayload") String jsonPayload,
                                  RedirectAttributes redirectAttributes) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<MemberDTO> members = mapper.readValue(jsonPayload, new TypeReference<List<MemberDTO>>() {
        });
        adminMemberService.AdminupdateRank(members);
        redirectAttributes.addFlashAttribute("msg", "선택된 회원 등급이 수정되었습니다.");
        return "redirect:/admin/member/list";
    }


}




