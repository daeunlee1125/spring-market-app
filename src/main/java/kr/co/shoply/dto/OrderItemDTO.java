package kr.co.shoply.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long item_No;
    private String ord_No;
    private String prod_No;
    private String item_Name;
    private Integer item_Cnt;
    private Integer item_Stat;
}
