package kr.co.shoply.service;

import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.dto.ReviewDTO;
import kr.co.shoply.entity.Product;
import kr.co.shoply.mapper.ProductMapper;
import kr.co.shoply.mapper.ReviewMapper;
import kr.co.shoply.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ReviewMapper reviewMapper;

    public List<ProductDTO> getProductAll(int cate2_no, String sort){
        return productMapper.selectAll(cate2_no, sort);
    }

}
