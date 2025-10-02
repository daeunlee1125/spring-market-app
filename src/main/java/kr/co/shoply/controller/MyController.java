package kr.co.shoply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyController {

    @GetMapping("/my/home")
    public String home() {
        log.info("home() GET 요청...");
        return "my/home";
    }

    @GetMapping("/my/order")
    public String order() {
        log.info("order() GET 요청...");
        return "my/order";
    }

    @GetMapping("/my/point")
    public String point() {
        log.info("point() GET 요청...");
        return "my/point";
    }

    @GetMapping("/my/coupon")
    public String coupon() {
        log.info("coupon() GET 요청...");
        return "my/coupon";
    }

    @GetMapping("/my/review")
    public String review() {
        log.info("review() GET 요청...");
        return "my/review";
    }

    @GetMapping("/my/qna")
    public String qna() {
        log.info("qna() GET 요청...");
        return "my/qna";
    }

    @GetMapping("/my/info")
    public String info() {
        log.info("info() GET 요청...");
        return "my/info";
    }
}