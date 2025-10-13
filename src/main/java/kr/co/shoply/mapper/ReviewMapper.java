package kr.co.shoply.mapper;

import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductDTO;
import kr.co.shoply.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    // 사용
    public List<ReviewDTO> selectAll3(String prod_no);
    public List<ReviewDTO> selectPageList3(@Param("prod_no") String prod_no, @Param("offset") int offset); // 해당 상품 리뷰 페이지네이션
    public int selectCountAll3(String prod_no); // 해당 상품 리뷰 개수

    public void insert(ReviewDTO reviewDTO);
    public void update(ReviewDTO reviewDTO);
    public void delete(int rev_no);
}
