package kr.co.shoply.mapper;


import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IndexMapper {

    // 최신상품
    List<ProductDTO> selectNewProducts();

    // 히트상품
    //List<ProductDTO> selectHitProducts();

    // 추천상품
    // List<ProductDTO> selectRecomProducts();
}
