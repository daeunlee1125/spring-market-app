package kr.co.shoply.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.shoply.dto.*;
import kr.co.shoply.mapper.AdminMemberMapper;
import kr.co.shoply.service.AdminMemberService;
import kr.co.shoply.service.SiteInfoService;
import kr.co.shoply.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMemberController {

  private final AdminMemberService adminMemberService;

    
  private final AdminMemberMapper adminMemberMapper;
  private final VersionService versionService;
  private final SiteInfoService siteInfoService;


    @GetMapping("/admin/member/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<MemberDTO> pageResponseDTO = adminMemberService.selectMembers(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);


        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);
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

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        // 3. 모델에 추가
        model.addAttribute("selectedMember", selectedMember);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("openModal", true); // 모달 표시 플래그



        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

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






    @GetMapping("/admin/member/point")
    public String pointList(PageRequestDTO pageRequestDTO, Model model) {

        // 1. 포인트 리스트 조회 (페이징 포함)
        List<PointDTO> pointList = adminMemberMapper.selectAdminPointList(pageRequestDTO);

        // 2. 전체 개수 조회 (페이징용)
        int total = adminMemberMapper.selectAdminTotalCount(pageRequestDTO);

        // 3. 모델에 데이터 세팅
        model.addAttribute("pointList", pointList);
        model.addAttribute("pageResponseDTO",
                PageResponseDTO.<PointDTO>builder()
                        .pageRequestDTO(pageRequestDTO)
                        .total(total)
                        .build());

        SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
        model.addAttribute("siteInfoDTO", siteInfoDTO);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/member/point";
    }




    @PostMapping("/admin/member/point/delete")
    @ResponseBody
    public ResponseEntity<?> deleteSelectedPoints(@RequestBody List<Integer> p_no) {
        try {
            adminMemberMapper.deleteSelectedPoints(p_no);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }


}




