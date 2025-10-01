package kr.co.shoply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    @GetMapping("/product/list")
    public String list() {
        return "product/list";
    }

    @GetMapping("/product/view")
    public String view() {
        return "product/view";
    }

    @GetMapping("/product/cart")
    public String cart() {
        return "product/cart";
    }

    @GetMapping("/product/complete")
    public String complete() {
        return "product/complete";
    }

    @GetMapping("/product/order")
    public String order() {
        return "product/order";
    }

    @GetMapping("/product/search")
    public String search() {
        return "product/search";
    }



}
