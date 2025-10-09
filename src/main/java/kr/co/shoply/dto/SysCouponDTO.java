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
    private Integer cp_type;
    private Integer cp_value;
    private Integer cp_min_price;
    private Date cp_issue_date;
    private Date cp_exp_date;
}
