package kr.co.shoply.mapper;

import kr.co.shoply.dto.ProFileDTO;
import kr.co.shoply.dto.ProdOptionDTO;
import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyProductMapper {
    ProductDTO selectProduct(@Param("prod_no") String prod_no);
    List<ProdOptionDTO> selectOption(@Param("prod_no") String prod_no);
    List<ProFileDTO> selectFiles(@Param("prod_no") String prod_no);

}
