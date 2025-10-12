package kr.co.shoply.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER_COUPON")
public class UserCoupon{

    @Id
    private String cp_no;


    private String cp_code;
    private String mem_id;
    private String cp_issued_date;
    private String cp_used_date;
    private int cp_stat;
}
