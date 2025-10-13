package kr.co.shoply.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.shoply.dto.*;
import kr.co.shoply.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MyService myService;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        log.info("home() GET 요청...");

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {
            return "redirect:/member/login";
        }

        String mem_id = sessUser.getMem_id();
        MyPageHomeDTO homeData = myService.getMyPageHomeData(mem_id);

        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());
        model.addAttribute("recentOrders", homeData.getRecentOrders());
        model.addAttribute("recentPoints", homeData.getRecentPoints());
        model.addAttribute("recentReviews", homeData.getRecentReviews());
        model.addAttribute("recentQnas", homeData.getRecentQnas());
        model.addAttribute("sessUser", sessUser);

        return "my/home";
    }

    @GetMapping("/info")
    public String info(Model model, HttpSession session) {
        log.info("info() GET 요청...");

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        if (sessUser == null) {
            return "redirect:/member/login";
        }

        String mem_id = sessUser.getMem_id();
        MemberDTO memberInfo = myService.getMemberInfo(mem_id);

        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("sessUser", sessUser);

        return "my/info";
    }

    @PostMapping("/info/update")
    @ResponseBody
    public ResponseEntity<String> updateInfo(@RequestBody MemberDTO memberDTO, HttpSession session) {
        log.info("updateInfo() POST 요청...");

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        memberDTO.setMem_id(sessUser.getMem_id());
        myService.updateMemberInfo(memberDTO);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/info/changePassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody String newPassword, HttpSession session) {
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        myService.changePassword(sessUser.getMem_id(), newPassword);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/info/withdrawal")
    public String withdrawal(HttpSession session) {
        log.info("withdrawal() POST 요청...");

        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return "redirect:/member/login";
        }

        myService.withdrawMember(sessUser.getMem_id());
        session.invalidate();

        return "redirect:/member/login?withdrawal=true";
    }

    @PostMapping("/order/confirm")
    @ResponseBody
    public ResponseEntity<String> confirmOrder(@RequestParam("item_no") Long itemNo, HttpSession session) {
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        myService.confirmOrder(itemNo, sessUser.getMem_id());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/review/write")
    @ResponseBody
    public ResponseEntity<String> writeReview(@ModelAttribute ReviewDTO reviewDTO, HttpSession session) {
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        reviewDTO.setMem_id(sessUser.getMem_id());
        myService.writeReview(reviewDTO);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/qna/write")
    @ResponseBody
    public ResponseEntity<String> writeQna(@ModelAttribute QnaDTO qnaDTO, HttpSession session) {
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");
        if (sessUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        qnaDTO.setMem_id(sessUser.getMem_id());
        myService.writeQna(qnaDTO);
        return ResponseEntity.ok("success");
    }
}