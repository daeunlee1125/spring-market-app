package kr.co.shoply.controller;

import kr.co.shoply.dto.BannerDTO;
import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.TermsDTO;
import kr.co.shoply.service.EmailService;
import kr.co.shoply.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
    @RequiredArgsConstructor
    @Controller
    public class MemberController {

        private final MemberService memberService;
        private final EmailService emailService;

        @GetMapping("/member/join")
        public String join(){
            return "member/join";
        }

        @GetMapping("/member/login")
        public String login(Model model){
            List<BannerDTO> banner = memberService.getLogBan2();
            model.addAttribute("banners",banner);
            return "member/login";
        }

        @GetMapping("/member/register")
        public String register(){
            return "member/register";
        }

        @PostMapping("/member/register")
        public String register(MemberDTO memberDTO){

            log.info("MemberDTO = {}", memberDTO);

            memberService.insertMember(memberDTO);

            return "redirect:/member/login";
        }

        @GetMapping("/member/registerSeller")
        public String registerSeller(){
            return "member/registerSeller";
        }

        @PostMapping("/member/registerSeller")
        public String registerSeller(MemSellerDTO memSellerDTO){
            log.info("MemSellerDTO = {}", memSellerDTO);

            memberService.insertSeller(memSellerDTO);

            return "redirect:/member/login";
        }

        @GetMapping("/member/signup")
        public String signup(@RequestParam(name = "type", required = false) String type, Model model){

            TermsDTO termsDTO = memberService.getTerms();

            model.addAttribute("type", type);
            model.addAttribute("termsDTO", termsDTO);

            return "member/signup";
        }

        @GetMapping("/member/checkId")
        @ResponseBody
        public Map<String, String> checkId(@RequestParam String mem_id) {
            Map<String, String> result = new HashMap<>();

            boolean exists = memberService.checkIdExists(mem_id);

            if (exists) {
                result.put("result", "fail");
            } else {
                result.put("result", "success");
            }

            return result;
        }

        // 이메일 인증번호 발송
        @ResponseBody
        @PostMapping("/member/email")
        public ResponseEntity<Map<String, Integer>> verifyEmail(@RequestBody Map<String, Object> jsonData){

            String mem_email = (String) jsonData.get("email");
            log.info("mem_email = {}", mem_email);

            int count = emailService.countEmail(mem_email);

            if(count == 0){
                emailService.sendCode(mem_email);
            }

            return ResponseEntity.ok(Map.of("count", count));

        }

    }
