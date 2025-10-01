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
    @Column(name = "cp_no", length = 11)
    private String cpNo;

    @Column(name = "cp_code", length = 11, nullable = false)
    private String cpCode;

    @Column(name = "mem_id", length = 30, nullable = false)
    private String memId;

    @Column(name = "cp_issued_date", nullable = false)
    private Date cpIssuedDate;

    @Column(name = "cp_used_date")
    private Date cpUsedDate;

    @Column(name = "cp_stat")
    private Integer cpStat;
}