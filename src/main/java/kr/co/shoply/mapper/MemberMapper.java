package kr.co.shoply.mapper;

import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 사용
    public MemberDTO selectADDR3(@Param("mem_id") String mem_id, @Param("prod_no") String prod_no); // view.html에 판매자 주소 출력 용도
}
