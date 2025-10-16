package kr.co.shoply.service;

import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.mapper.IndexMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final IndexMapper indexMapper;

    public List<ProductDTO> getNewProducts() {
        List<ProductDTO> products = indexMapper.selectNewProducts();
        // log.info("상품 이미지 경로들: {}", products.stream().map(ProductDTO::getF_name).toList());
        return products;
    }

    public List<ProductDTO> getHitProducts() {
        List<ProductDTO> hitProducts = indexMapper.selectHitProducts();
        return hitProducts;
    }


    public List<ProductDTO> getRecommendedProducts() {
        List<ProductDTO> recommended = indexMapper.selectRecommendedProducts();
        return recommended;
    }

    public List<ProductDTO> getBestProducts() {
        List<ProductDTO> products = indexMapper.selectBestProducts();
        return products;
    }


    // 💸 할인상품 (할인율 높은 순)
    public List<ProductDTO> getDiscountProducts() {
        List<ProductDTO> products = indexMapper.selectDiscountProducts();
        return products;
    }

}
