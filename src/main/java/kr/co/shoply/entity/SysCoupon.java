package kr.co.shoply.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import jakarta.persistence.Entity;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SYS_COUPON")
public class SysCoupon {

    @Id
    private String cp_code;


    private String cp_name;
    private Integer cp_type;
    private Integer cp_value;
    private Integer cp_min_price;
    private LocalDate cp_issue_date;
    private LocalDate cp_exp_date;
    private Integer cp_stat;

    private String cp_issuer_id;
    private String cp_issuer_name; // 발급자 이름


    private int issuecount; //쿠폰 발급 수
    private int usedcount; // 쿠폰 사용 수

}
