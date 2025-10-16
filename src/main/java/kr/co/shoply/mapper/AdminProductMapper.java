package kr.co.shoply.mapper;

import kr.co.shoply.dto.ProFileDTO;
import kr.co.shoply.dto.ProdNoticeDTO;
import kr.co.shoply.dto.ProdOptionDTO;
import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminProductMapper {
    int insertProduct(ProductDTO dto);
    int insertProductFile(ProFileDTO dto);
    int insertProductNotice(ProdNoticeDTO dto);
    int insertProductOption(ProdOptionDTO dto);
}
