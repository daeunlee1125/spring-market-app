package kr.co.shoply.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MyPageHomeDTO {
    private int orderCount;
    private int couponCount;
    private int pointTotal;
    private int qnaCount;
    private List<OrderItemDTO> recentOrders;
    private List<PointDTO> recentPoints;
    private List<ReviewDTO> recentReviews;
    private List<QnaDTO> recentQnas;
}