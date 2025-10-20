package kr.co.shoply.service;

import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.mapper.CartMapper;
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
    private final CartMapper cartMapper;

    public List<ProductDTO> getNewProducts() {
        // log.info("상품 이미지 경로들: {}", products.stream().map(ProductDTO::getF_name).toList());
        return indexMapper.selectNewProducts();
    }

    public List<ProductDTO> getHitProducts() {
        return indexMapper.selectHitProducts();
    }


    public List<ProductDTO> getRecommendedProducts() {
        return indexMapper.selectRecommendedProducts();
    }

    public List<ProductDTO> getBestProducts() {
        return indexMapper.selectBestProducts();
    }


    //  할인상품 (할인율 높은 순)
    public List<ProductDTO> getDiscountProducts() {
        return indexMapper.selectDiscountProducts();
    }


    // 베스트상품 (왼쪽 사이드)
    public List<ProductDTO> getSidebarBestProducts() {
        return indexMapper.selectSidebarBestProducts();
    }

    public int getCartCount3(String memId) {
        return cartMapper.selectCartIndex3(memId);
    }


}
