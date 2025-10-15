package kr.co.shoply.controller;

import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.service.EmailService;
import kr.co.shoply.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberFindController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/member/find/userId")
    public String userId(){
        return "member/find/userId";
    }

    @GetMapping("/member/find/password")
    public String password(){
        return "member/find/password";
    }

    // 이메일 인증번호 발송
    @ResponseBody
    @PostMapping("/member/find/email")
    public ResponseEntity<Map<String, Integer>> verifyEmail(@RequestBody Map<String, Object> jsonData){
        String mem_name = (String) jsonData.get("name");
        String mem_email = (String) jsonData.get("email");

        int count = emailService.countUser(mem_name, mem_email);

        if(count > 0){
            emailService.sendCode(mem_email);
        }

        return ResponseEntity.ok(Map.of("count", count));

    }

    // 비밀번호 찾기용
    @ResponseBody
    @PostMapping("/member/find/emailPass")
    public ResponseEntity<Map<String, Integer>> verifyEmailForPassword(@RequestBody Map<String, Object> jsonData){
        String mem_id = (String) jsonData.get("id");
        String mem_email = (String) jsonData.get("email");

        int count = emailService.countUserByIdAndEmail(mem_id, mem_email);

        if(count > 0){
            emailService.sendCode(mem_email);
        }

        return ResponseEntity.ok(Map.of("count", count));
    }

    // 인증번호 확인
    @ResponseBody
    @PostMapping("/member/find/verifyCode")
    public ResponseEntity<Map<String, Boolean>> verifyCode(@RequestBody Map<String, Object> jsonData){
        String code = (String) jsonData.get("code");
        boolean isValid = emailService.verifyCode(code);

        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    @PostMapping("/member/find/resultId")
    public String resultId(@RequestParam("mem_name") String mem_name, @RequestParam("mem_email") String mem_email, Model model){

        log.info("아이디 찾기 결과 조회 - 이름: {}, 이메일: {}", mem_name, mem_email);

        MemberDTO memberDTO = memberService.findMemberByNameAndEmail(mem_name, mem_email);

        log.info("조회 결과: {}", memberDTO);
        if(memberDTO != null) {
            model.addAttribute("member", memberDTO);
        }

        return "member/find/resultId";
    }

    @GetMapping("/member/find/changePassword")
    public String changePassword(@RequestParam("mem_id") String mem_id, Model model){
        log.info("비밀번호 변경 페이지 - 아이디: {}", mem_id);

        model.addAttribute("mem_id", mem_id);

        return "member/find/changePassword";
    }

    @PostMapping("/member/find/changePassword")
    public String changePassword(MemberDTO memberDTO){
        log.info("비밀번호 변경 페이지 - memberDTO: {}", memberDTO);

        boolean isSuccess = memberService.modifyPassword(memberDTO);

        if(isSuccess) {
            return "redirect:/member/login?success=success";
        } else {
            return "member/find/changePassword";
        }
    }
}
