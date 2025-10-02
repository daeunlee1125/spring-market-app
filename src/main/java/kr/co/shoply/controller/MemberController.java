package kr.co.shoply.controller;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.TermsDTO;
import kr.co.shoply.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/join")
    public String join(){
        return "member/join";
    }

    @GetMapping("/member/login")
    public String login(){
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
}
