package kr.co.shoply.mapper;

import kr.co.shoply.dto.AdInfoDTO;
import kr.co.shoply.dto.CompleteDTO;
import kr.co.shoply.dto.OrderDTO;
import kr.co.shoply.dto.OrderItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    public AdInfoDTO ordStatCnt2();
    void insertOrder3(String mem_id, String ord_name, String ord_hp, String ord_zip, String ord_addr1, String ord_addr2, int ord_total);
    OrderDTO selectOrderNo3(String mem_id);
    OrderDTO selectOrderById(String ord_no);

    void insertOrderItemList3(List<OrderItemDTO> list);

    List<CompleteDTO> selectCompleteOrder3(@Param("ord_no") String ord_no, @Param("cart_no_list") List<Integer> cart_no_list);
}
