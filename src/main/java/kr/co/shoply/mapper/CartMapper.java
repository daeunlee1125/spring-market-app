package kr.co.shoply.mapper;

import kr.co.shoply.dto.CartDTO;
import kr.co.shoply.dto.CartUpdateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    public List<CartDTO> selectCartList3(@Param("mem_id") String mem_id); // 해당 아이디 장바구니 select
    public int selectCartCount3(@Param("mem_id") String mem_id, @Param("prod_no") String prod_no, @Param("cart_option") String cart_option);
    public List<CartDTO> selectCartsByNos(List<Integer> cart_no_list); // 장바구니에서 선택된 상품 list
    int selectCartIndex3(String mem_id);
    public void insertCart3(String mem_id, String prod_no, int cart_item_cnt, String cart_option); // 장바구니 삽입
    public void deleteCart3(int cart_no);
    public void deleteSelectedCarts3(List<Integer> cart_no_list);
    public void updateCartQuantity(CartUpdateDTO cartUpdateDTO);
}
