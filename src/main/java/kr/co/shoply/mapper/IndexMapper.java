package kr.co.shoply.mapper;


import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IndexMapper {

    // 최신상품
    List<ProductDTO> selectNewProducts();

    // 히트상품
    List<ProductDTO> selectHitProducts();

    // 추천상품
    List<ProductDTO> selectRecommendedProducts();

    // 인기상품
    List<ProductDTO> selectBestProducts();

    // 할인상품
    List<ProductDTO> selectDiscountProducts();

    // 사이드 베스트상품
    List<ProductDTO> selectSidebarBestProducts();

}
