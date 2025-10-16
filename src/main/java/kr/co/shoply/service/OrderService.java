package kr.co.shoply.service;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.OrderDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
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
}
