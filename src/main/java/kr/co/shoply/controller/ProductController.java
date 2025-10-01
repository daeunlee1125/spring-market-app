package kr.co.shoply.controller;

import kr.co.shoply.dto.ProductDTO;
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

    @GetMapping("/product/list/{cate2No}")
    public String list(@PathVariable int cate2No, Model model) {
        List<ProductDTO> productDTOList = productService.getProductAll(cate2No);
        log.info("productDTOList={}", productDTOList);
        model.addAttribute(productDTOList);

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
