package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDTO {
    private String cp_no;

    private String cp_code;
    private String mem_id;
    private String cp_issued_date;
    private String cp_used_date;
    private int cp_stat;

}
