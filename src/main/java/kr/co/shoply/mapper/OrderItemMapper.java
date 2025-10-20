package kr.co.shoply.mapper;

import kr.co.shoply.dto.AdInfoDTO;
import kr.co.shoply.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    public AdInfoDTO itemStatCnt2();
    public void setItemDelvInfo2(OrderDTO orderDTO);
    public void updateOrderItem();
}
