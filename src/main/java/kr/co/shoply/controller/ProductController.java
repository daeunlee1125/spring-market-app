package kr.co.shoply.controller;

import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.dto.ReviewDTO;
import kr.co.shoply.service.Cate2Service;
import kr.co.shoply.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final Cate2Service cate2Service;

    @GetMapping("/product/list/{cate2No}")
    public String list(@PathVariable int cate2No, Model model) {
        return "redirect:/product/list/" + cate2No + "/sold";
    }

    @GetMapping("/product/list/{cate2No}/{sort}")
    public String sortList(@PathVariable int cate2No, @PathVariable String sort, Model model) {
        List<ProductDTO> productDTOList = productService.getProductAll(cate2No, sort);

        Cate2DTO cate2DTO = cate2Service.getCate(cate2No);

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("cate2DTO", cate2DTO);
        model.addAttribute("sort", sort);

        return "product/list";
    }

    @GetMapping("/product/view/{prodNo}")
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
