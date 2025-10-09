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
@Table(name = "user_coupon")
public class UserCoupon {
    @Id
    private String cp_no;
    private String cp_code;
    private String mem_id;
    private Date cp_issued_date;
    private Date cp_used_date;
    private Integer cp_stat;
}