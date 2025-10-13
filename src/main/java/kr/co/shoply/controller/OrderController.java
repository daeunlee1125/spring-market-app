package kr.co.shoply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@RequiredArgsConstructor
@Controller
public class OrderController {

    @GetMapping("/admin/order/list")
    public String orderList() {

        return "admin/order/list";
    }

    @GetMapping("/admin/order/delivery")
    public String orderDelivery() {

        return "admin/order/delivery";
    }


}
