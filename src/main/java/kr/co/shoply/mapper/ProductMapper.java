package kr.co.shoply.mapper;

import kr.co.shoply.dto.ProFileDTO;
import kr.co.shoply.dto.ProdOptionDTO;
import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 사용
    public List<ProductDTO> selectAll3(@Param("cate2_no") int cate2_no, @Param("sort") String sort); // 카테고리별 select
    public ProductDTO getProductDetail(String prod_no); // pk 이용해서 단건 조회
    public List<ProdOptionDTO> selectOption3(String prod_no);
    public List<ProFileDTO> selectFiles3(String prod_no);
    List<ProductDTO> selectSearchProduct3(String keyword, String sort);
    List<ProductDTO> selectSearch2Product3(@Param("keyword") String keyword,
                                          @Param("sort") String sort,
                                          @Param("type") String type,
                                          @Param("keyword2") String keyword2,
                                          @Param("startPrice") Integer startPrice,
                                          @Param("endPrice") Integer endPrice);

    public void insert(ProductDTO productDTO);
    public void update(ProductDTO productDTO);
    public void delete(int prod_no);
}
