package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDTO {
    private int p_no;
    private String mem_id;
    private int p_type;
    private int p_point;
    private String p_info;
    private String p_date;
    private String p_exp_date;
    private String ord_no;

    // 잔여포인트
    private int remain_point;

    private String mem_name;

    // ✅ 추가: 주문정보
    private String prod_no;
    private String prod_name;
    private Integer prod_price;
    private Integer item_cnt;
    private Integer item_stat;
    private String item_stat_name;  // 상태명 (배송완료, 구매확정 등)
}
