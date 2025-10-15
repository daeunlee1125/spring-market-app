package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/list")
    public String list(){
        return "admin/product/list";
    }

    @GetMapping("/register")
    public String register(Model model){
        List<Cate1DTO> cate1DTOList = adminProductService.getAllCate1();
        log.info("cate1DTOList={}",cate1DTOList);

        model.addAttribute("cate1DTOList",cate1DTOList);

        return "admin/product/register";
    }

    @GetMapping("/{cate1}/cate2")
    @ResponseBody
    public List<Cate2DTO> getCate2(@PathVariable String cate1){
        log.info("cate1={}",cate1);

        return adminProductService.getCate2ByCate1(cate1);
    }
}
