package kr.co.shoply.mapper;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Cate2Mapper {

    public Cate2DTO select(int cate2_no);
    public List<ProductDTO> selectAll();
    public void insert(ProductDTO productDTO);
    public void update(ProductDTO productDTO);
    public void delete(int prod_no);
}
