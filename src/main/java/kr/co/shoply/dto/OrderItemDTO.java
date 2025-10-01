package kr.co.shoply.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long itemNo;
    private String ordNo;
    private String prodNo;
    private String itemName;
    private Integer itemCnt;
    private Integer itemStat;
}
