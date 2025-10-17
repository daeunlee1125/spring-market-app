package kr.co.shoply.service;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.OrderDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.mapper.OrderItemMapper;
import kr.co.shoply.mapper.OrderMapper;
import kr.co.shoply.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public void setDelvs(OrderDTO orderDTO) {
        orderMapper.setOrdDelvInfo2(orderDTO);
        orderItemMapper.setItemDelvInfo2(orderDTO);
    }

    public PageResponseDTO getOrders2(PageRequestDTO pageRequestDTO) {

        List<OrderDTO> ordList = orderMapper.getOrderList2(pageRequestDTO);
        int total = orderMapper.selectCountTotal2(pageRequestDTO);

        return PageResponseDTO.<OrderDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(ordList)
                .total(total)
                .build();
    }

    public List<OrderDTO> OrderDetails2() {
        return orderMapper.getOrderDetails2();
    }

    public PageResponseDTO getDelivs2(PageRequestDTO pageRequestDTO) {

        List<OrderDTO> ordList = orderMapper.getDelivList2(pageRequestDTO);
        int total = orderMapper.selectCountDelvTotal2(pageRequestDTO);

        return PageResponseDTO.<OrderDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(ordList)
                .total(total)
                .build();
    }

    public List<OrderDTO> delivDetails2() {return orderMapper.getDelivDetails();}
}
