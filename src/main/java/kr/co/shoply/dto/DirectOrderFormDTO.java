package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 바로구매 Form 요청 전체를 감싸는 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectOrderFormDTO {
    // JavaScript의 orderItems 배열을 받기 위한 필드
    private List<DirectOrderItemDTO> orderItems;
}