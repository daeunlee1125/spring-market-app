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
        log.info("상품 이미지 경로들: {}", products.stream().map(ProductDTO::getF_name).toList());

        return products;
    }

}
