package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductListDTO;
import kr.co.shoply.dto.ProductRegisterDTO;
import kr.co.shoply.security.MyUserDetails;
import kr.co.shoply.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @ResponseBody
    @GetMapping("/logs")
    public ResponseEntity<String> getLogFile() throws Exception {

        Path localPath = Paths.get("logs/shoply.log");
        Path ec2Path = Paths.get("/home/ec2-user/shoply/logs/shoply.log");

        Path path = Files.exists(localPath) ? localPath : ec2Path;
        path = path.toAbsolutePath();

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String content = Files.readString(path, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"shoply.log\"")
                .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                .body(content);
    }

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
    public String register(ProductRegisterDTO productRegisterDTO, Principal principal, RedirectAttributes redirectAttributes){
        log.info("productRegisterDTO={}",productRegisterDTO);
        adminProductService.registerProduct(productRegisterDTO, principal.getName());

        redirectAttributes.addFlashAttribute("showAlert", true);

        return "redirect:/admin/product/register";

    }

    @GetMapping("/{cate1}/cate2")
    @ResponseBody
    public List<Cate2DTO> getCate2(@PathVariable String cate1){
        log.info("cate1={}",cate1);

        return adminProductService.getCate2ByCate1(cate1);
    }
}
