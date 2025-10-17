package kr.co.shoply.mapper;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Mapper
public interface OrderMapper {
    public AdInfoDTO ordStatCnt2();
    void insertOrder3(String mem_id, String ord_name, String ord_hp, String ord_zip, String ord_addr1, String ord_addr2, String ord_payment, int ord_total);
    OrderDTO selectOrderNo3(String mem_id);
    OrderDTO selectOrderById(String ord_no);

    void insertOrderItemList3(List<OrderItemDTO> orderItemDTOList);

    List<CompleteDTO> selectCompleteOrder3(@Param("ord_no") String ord_no);

    public List<OrderDTO> getOrderList2(PageRequestDTO pageRequestDTO);
    public int selectCountTotal2(PageRequestDTO pageRequestDTO);
    public List<OrderDTO> getOrderDetails2();
    public List<OrderDTO> getDelivList2(PageRequestDTO pageRequestDTO);
    public int selectCountDelvTotal2(PageRequestDTO pageRequestDTO);
    public void setOrdDelvInfo2(OrderDTO orderDTO);
    public List<OrderDTO> getDelivDetails();

}
