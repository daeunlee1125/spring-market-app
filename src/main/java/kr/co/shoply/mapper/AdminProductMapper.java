package kr.co.shoply.mapper;

import kr.co.shoply.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductMapper {

    // 상품목록
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

    // 상품등록
    int insertProduct(ProductDTO dto);
    int insertProductFile(ProFileDTO dto);
    int insertProductNotice(ProdNoticeDTO dto);
    int insertProductOption(ProdOptionDTO dto);

    // 상품목록 삭제
    String selectProductOwner(@Param("prodNo") String prodNo);
    List<ProFileDTO> selectProductFiles(@Param("prodNo") String prodNo);
    int deleteProductOptions(@Param("prodNo") String prodNo);
    int deleteProductNotices(@Param("prodNo") String prodNo);
    int deleteProductFiles(@Param("prodNo") String prodNo);
    int deleteProduct(@Param("prodNo") String prodNo);
}
