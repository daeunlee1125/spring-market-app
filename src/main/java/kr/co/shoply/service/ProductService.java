package kr.co.shoply.service;

import kr.co.shoply.dto.ProdOptionDTO;
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

    public List<ProductDTO> getProductAll3(int cate2_no, String sort){
        return productMapper.selectAll3(cate2_no, sort);
    }

    public ProductDTO getProduct3(int prod_no){
        return productMapper.select3(prod_no);
    }

    public List<ProdOptionDTO> getProductOption3(int prod_no){


        return productMapper.selectOption3(prod_no);
    }
}
