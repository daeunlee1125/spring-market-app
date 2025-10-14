package kr.co.shoply.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDTO{
    private String cp_no;
    private String cp_code;
    private String mem_id;
    private Date cp_issued_date;
    private Date cp_used_date;
    private Integer cp_stat;


    private int cp_type;
    private int cp_value;

    //sys_coupon
    private String cp_note;
    private int cp_min_price;


    private String mem_name;
    private String cp_issuer_name;
    private String cp_name;


    private Date cp_exp_date; // 만료일

}
