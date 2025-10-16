package kr.co.shoply.mapper;

import kr.co.shoply.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductMapper {
    List<ProductListDTO> selectProductList(@Param("memId") String memId, @Param("memLevel") int memLevel);
    int insertProduct(ProductDTO dto);
    int insertProductFile(ProFileDTO dto);
    int insertProductNotice(ProdNoticeDTO dto);
    int insertProductOption(ProdOptionDTO dto);
}
