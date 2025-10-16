package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductListDTO;
import kr.co.shoply.dto.ProductRegisterDTO;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal MyUserDetails user) {

        String memId = user.getUsername();
        int memLevel =  user.getMemLevel();

        log.info("memId:{} memLevel:{}", memId, memLevel);

        List<ProductListDTO> products = adminProductService.getProductList(memId, memLevel);

        log.info("products:{}", products);

        model.addAttribute("products", products);

        return "admin/product/list";
    }

    @GetMapping("/register")
    public String register(Model model){
        List<Cate1DTO> cate1DTOList = adminProductService.getAllCate1();
        log.info("cate1DTOList={}",cate1DTOList);

        model.addAttribute("cate1DTOList",cate1DTOList);

        return "admin/product/register";
    }

    @PostMapping("/register")
    public String register(ProductRegisterDTO productRegisterDTO, Principal principal){
        log.info("productRegisterDTO={}",productRegisterDTO);
        adminProductService.registerProduct(productRegisterDTO, principal.getName());

        return "/admin/product/register";
    }

    @GetMapping("/{cate1}/cate2")
    @ResponseBody
    public List<Cate2DTO> getCate2(@PathVariable String cate1){
        log.info("cate1={}",cate1);

        return adminProductService.getCate2ByCate1(cate1);
    }
}
