package kr.co.shoply.controller;

import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProdOptionDTO;
import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.dto.ReviewDTO;
import kr.co.shoply.service.Cate2Service;
import kr.co.shoply.service.ProductService;
import kr.co.shoply.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final Cate2Service cate2Service;
    private final ReviewService reviewService;

    @GetMapping("/product/list/{cate2No}")
    public String list(@PathVariable int cate2No, Model model) {
        return "redirect:/product/list/" + cate2No + "/sold";
    }

    @GetMapping("/product/list/{cate2No}/{sort}")
    public String sortList(@PathVariable int cate2No, @PathVariable String sort, Model model) {
        List<ProductDTO> productDTOList = productService.getProductAll3(cate2No, sort);

        Cate2DTO cate2DTO = cate2Service.getCate(cate2No);

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("cate2DTO", cate2DTO);
        model.addAttribute("sort", sort);

        return "product/list";
    }

    @GetMapping("/product/view/{cate2No}/{prodNo}")
    public String view(@PathVariable int cate2No, @PathVariable String prodNo, Model model) {
        Cate2DTO cate2DTO = cate2Service.getCate(cate2No);
        model.addAttribute("cate2DTO", cate2DTO);

        ProductDTO productDTO = productService.getProduct3(prodNo);
        model.addAttribute("productDTO", productDTO);

        List<ReviewDTO> reviewDTOList = reviewService.getReviews3(prodNo);
        model.addAttribute("reviewDTOList", reviewDTOList);

        List<ProdOptionDTO> OpDtoList = productService.getProductOption3(prodNo); // 상품별 옵션들
        for(ProdOptionDTO opDto : OpDtoList){
            // 콤마(,)로 구분된 문자열을 List<String>으로 변환
            List<String> values = Arrays.asList(opDto.getOpt_val().split("\\s*,\\s*"));
            opDto.setOptValList(values);
        }
        model.addAttribute("OpDtoList", OpDtoList);

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
