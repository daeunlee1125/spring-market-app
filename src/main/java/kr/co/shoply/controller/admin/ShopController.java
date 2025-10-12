package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.service.MemSellerService;
import kr.co.shoply.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final MemberService memberService;
    private final MemSellerService memSellerService;

    @GetMapping("/admin/shop/list")
    public String shopList(Model model){

        List<MemSellerDTO> sellers = memSellerService.getMemSellers2();
        model.addAttribute("sellers", sellers);

        return "admin/shop/list";
    }

    @GetMapping("/admin/shop/sales")
    public String sales(){
        return "admin/shop/sales";
    }

}
