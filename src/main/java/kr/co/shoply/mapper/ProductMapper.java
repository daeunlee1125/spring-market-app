package kr.co.shoply.mapper;

import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    public ProductDTO select(int prod_no);
    public List<ProductDTO> selectAll(int cate2_no);
    public void insert(ProductDTO productDTO);
    public void update(ProductDTO productDTO);
    public void delete(int prod_no);
}
