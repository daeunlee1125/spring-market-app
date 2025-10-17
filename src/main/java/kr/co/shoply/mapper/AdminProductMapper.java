package kr.co.shoply.mapper;

import kr.co.shoply.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductMapper {
    int countProductList(
            @Param("memId") String memId,
            @Param("memLevel") int memLevel,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword
    );
    List<ProductListDTO> selectProductList(
            @Param("memId") String memId,
            @Param("memLevel") int memLevel,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword
    );
    int insertProduct(ProductDTO dto);
    int insertProductFile(ProFileDTO dto);
    int insertProductNotice(ProdNoticeDTO dto);
    int insertProductOption(ProdOptionDTO dto);
}
