package kr.co.shoply.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long item_no;
    private String ord_no;
    private String prod_no;
    private String item_name;
    private Integer item_cnt;
    private Integer item_stat;
}
