package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteDTO {
    private OrderDTO order;
    private OrderItemDTO orderItems;
}
