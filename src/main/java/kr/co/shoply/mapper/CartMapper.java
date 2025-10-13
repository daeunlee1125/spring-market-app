package kr.co.shoply.mapper;

import kr.co.shoply.dto.CartDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    public List<CartDTO> selectCartList3(@Param("mem_id") String mem_id); // 해당 아이디 장바구니 select
    public void insertCart3(String mem_id, String prod_no, int cart_item_cnt, String cart_option); // 장바구니 삽입
}
