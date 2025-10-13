package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.MemSellerService;
import kr.co.shoply.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final MemberService memberService;
    private final MemSellerService memSellerService;

    @GetMapping("/admin/shop/list")
    public String shopList(Model model, PageRequestDTO pageRequestDTO){

        PageResponseDTO<MemSellerDTO> sellers = memSellerService.getMemSellers2Page(pageRequestDTO);
        log.info(sellers.toString());
        model.addAttribute("pageResponseDTO", sellers);

        return "admin/shop/list";
    }

    @PostMapping("/admin/shop/list")
    public String shopList(MemberDTO memberDTO){

        memSellerService.modifyStat(memberDTO);

        return "redirect:/admin/shop/list";
    }

    @PostMapping("/admin/shop/delete")
    @ResponseBody
    public Map<String, Object> shopDelete(@RequestBody Map<String, List<String>> data) {
        List<String> mem_ids = data.get("mem_ids");

        for (String mem_id : mem_ids) {
            memSellerService.deleteSeller(mem_id);
        }

        return Map.of("success", true);
    }

    @PostMapping("/admin/shop/register")
    public String shopRegister(MemSellerDTO memSellerDTO){

        memSellerService.registerSeller(memSellerDTO);

        return  "redirect:/admin/shop/list";
    }

    @GetMapping("/admin/shop/sales")
    public String sales(){
        return "admin/shop/sales";
    }

}
