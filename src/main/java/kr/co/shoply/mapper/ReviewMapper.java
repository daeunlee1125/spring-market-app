package kr.co.shoply.mapper;

import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    // 사용
    public double select(String prod_no);


    public List<ReviewDTO> selectAll();
    public void insert(ReviewDTO reviewDTO);
    public void update(ReviewDTO reviewDTO);
    public void delete(int rev_no);
}
