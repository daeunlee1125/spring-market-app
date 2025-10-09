package kr.co.shoply.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Entity
@Table(name = "sys_coupon")
public class SysCoupon {
    @Id
    private String cp_code;
    private String cp_name;
    private Integer cp_type;
    private Integer cp_value;
    private Integer cp_min_price;
    private Date cp_issue_date;
    private Date cp_exp_date;
}