package kr.co.shoply.controller;

import kr.co.shoply.dto.*;
import kr.co.shoply.service.EmailService;
import kr.co.shoply.service.MemberService;
import kr.co.shoply.service.ProductService;
import kr.co.shoply.service.SiteInfoService;
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
        private final SiteInfoService siteInfoService;
    private final ProductService productService;

        @GetMapping("/member/join")
        public String join(Model model) {
            SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
            model.addAttribute("siteInfoDTO", siteInfoDTO);

            List<Cate1DTO> cate1DTOList = productService.getCate1List();

            for (Cate1DTO cate1 : cate1DTOList) {
                // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
                List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

                // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
                cate1.setSubCategories(subList);
            }

            model.addAttribute("cate1DTOList", cate1DTOList);

            return "member/join";
        }

        @GetMapping("/member/login")
        public String login(Model model){
            List<BannerDTO> banner = memberService.getLogBan2();
            model.addAttribute("banners",banner);

            SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
            model.addAttribute("siteInfoDTO", siteInfoDTO);

            List<Cate1DTO> cate1DTOList = productService.getCate1List();

            for (Cate1DTO cate1 : cate1DTOList) {
                // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
                List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

                // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
                cate1.setSubCategories(subList);
            }

            model.addAttribute("cate1DTOList", cate1DTOList);

            return "member/login";
        }

        @GetMapping("/member/register")
        public String register(Model model){
            SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
            model.addAttribute("siteInfoDTO", siteInfoDTO);

            List<Cate1DTO> cate1DTOList = productService.getCate1List();

            for (Cate1DTO cate1 : cate1DTOList) {
                // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
                List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

                // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
                cate1.setSubCategories(subList);
            }

            model.addAttribute("cate1DTOList", cate1DTOList);

            return "member/register";
        }

        @PostMapping("/member/register")
        public String register(MemberDTO memberDTO){

            log.info("MemberDTO = {}", memberDTO);

            memberService.insertMember(memberDTO);

            return "redirect:/member/login";
        }

        @GetMapping("/member/registerSeller")
        public String registerSeller(Model model){
            SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
            model.addAttribute("siteInfoDTO", siteInfoDTO);

            List<Cate1DTO> cate1DTOList = productService.getCate1List();

            for (Cate1DTO cate1 : cate1DTOList) {
                // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
                List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

                // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
                cate1.setSubCategories(subList);
            }

            model.addAttribute("cate1DTOList", cate1DTOList);

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

            SiteInfoDTO siteInfoDTO = siteInfoService.getSiteInfo3();
            model.addAttribute("siteInfoDTO", siteInfoDTO);

            List<Cate1DTO> cate1DTOList = productService.getCate1List();

            for (Cate1DTO cate1 : cate1DTOList) {
                // 3. 해당 1차 카테고리의 2차 카테고리 목록을 DB에서 조회합니다.
                List<Cate2DTO> subList = productService.getCate2List(cate1.getCate1_no());

                // 4. 조회한 2차 목록을 Cate1DTO에 주입(set)합니다.
                cate1.setSubCategories(subList);
            }

            model.addAttribute("cate1DTOList", cate1DTOList);

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
