package kr.co.shoply.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.shoply.dto.MyPageHomeDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.MemberSessionDTO;
import kr.co.shoply.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MyService myService;
    // private static final String DEFAULT_MEM_ID = "root";

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        log.info("home() GET 요청...");

        // 세션에서 로그인한 사용자 정보를 가져옵니다.
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        // 만약 로그인 상태가 아니라면 로그인 페이지로 리다이렉트합니다.
        if (sessUser == null) {
            return "redirect:/member/login";
        }

        // 로그인한 사용자의 ID를 가져와 데이터를 조회합니다.
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

        // 세션에 있는 사용자 정보로 모델에 추가적인 데이터를 담아둡니다.
        model.addAttribute("sessUser", sessUser);

        return "my/home";
    }

    @GetMapping("/info")
    public String info(Model model, HttpSession session) {
        log.info("info() GET 요청...");

        // 세션에서 로그인한 사용자 정보를 가져옵니다.
        MemberSessionDTO sessUser = (MemberSessionDTO) session.getAttribute("sessUser");

        // 만약 로그인 상태가 아니라면 로그인 페이지로 리다이렉트합니다.
        if (sessUser == null) {
            return "redirect:/member/login";
        }

        // 로그인한 사용자의 ID를 가져와 데이터를 조회합니다.
        String mem_id = sessUser.getMem_id();
        MemberDTO memberInfo = myService.getMemberInfo(mem_id);

        model.addAttribute("memberInfo", memberInfo);

        // 세션에 있는 사용자 정보로 모델에 추가적인 데이터를 담아둡니다.
        model.addAttribute("sessUser", sessUser);

        return "my/info";
    }
    /*
    private final MyService myService;

    private static final String DEFAULT_MEM_ID = "root";

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        log.info("home() GET 요청...");

        // MyService를 통해 'root' 사용자의 데이터 조회
        MyPageHomeDTO homeData = myService.getMyPageHomeData(DEFAULT_MEM_ID);

        model.addAttribute("orderCount", homeData.getOrderCount());
        model.addAttribute("couponCount", homeData.getCouponCount());
        model.addAttribute("pointTotal", homeData.getPointTotal());
        model.addAttribute("qnaCount", homeData.getQnaCount());
        model.addAttribute("recentOrders", homeData.getRecentOrders());
        model.addAttribute("recentPoints", homeData.getRecentPoints());
        model.addAttribute("recentReviews", homeData.getRecentReviews());
        model.addAttribute("recentQnas", homeData.getRecentQnas());

        // 세션에 MemberSessionDTO를 설정하는 부분은 실제 로그인 로직에서 처리하면 됩니다.
        // 여기서는 임시로 데이터를 모델에만 추가합니다.
        MemberSessionDTO sessUser = new MemberSessionDTO();
        sessUser.setMem_id(DEFAULT_MEM_ID);
        sessUser.setMem_name("홍길동");
        session.setAttribute("sessUser", sessUser);

        return "my/home";
    }

    @GetMapping("/info")
    public String info(Model model, HttpSession session) {
        log.info("info() GET 요청...");

        // DB에서 'root' 사용자의 MemberDTO를 조회
        MemberDTO memberInfo = myService.getMemberInfo(DEFAULT_MEM_ID);
        model.addAttribute("memberInfo", memberInfo);

        // 세션에 MemberSessionDTO를 설정
        MemberSessionDTO sessUser = new MemberSessionDTO();
        sessUser.setMem_id(DEFAULT_MEM_ID);
        sessUser.setMem_name(memberInfo.getMem_name()); // DB에서 가져온 이름으로 설정
        session.setAttribute("sessUser", sessUser);

        return "my/info";


    }
    */
}