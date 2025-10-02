package kr.co.shoply.mapper;

import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 사용
    public List<ProductDTO> selectAll(@Param("cate2_no") int cate2_no, @Param("sort") String sort); // 일반 카테고리별 select



    public ProductDTO select(int prod_no);
    public void insert(ProductDTO productDTO);
    public void update(ProductDTO productDTO);
    public void delete(int prod_no);
}
