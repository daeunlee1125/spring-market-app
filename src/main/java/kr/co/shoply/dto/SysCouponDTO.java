package kr.co.shoply.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysCouponDTO {
    private String cp_code;
    private String cp_name;
    private int cp_type;
    private int cp_value;
    private int cp_min_price;
    private String cp_issue_date;
    private String cp_exp_date;
}
