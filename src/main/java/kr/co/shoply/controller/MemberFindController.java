package kr.co.shoply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberFindController {

    @GetMapping("/member/find/userId")
    public String userID(){
        return "member/find/userId";
    }

    @GetMapping("/member/find/password")
    public String password(){
        return "member/find/password";
    }

    @GetMapping("/member/find/resultId")
    public String resultId(){
        return "member/find/resultId";
    }

    @GetMapping("/member/find/changePassword")
    public String changePassword(){
        return "member/find/changePassword";
    }
}
