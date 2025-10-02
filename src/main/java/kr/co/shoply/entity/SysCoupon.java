package kr.co.shoply.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "SYS_COUPON")
public class SysCoupon {

    @Id
    private String cp_code;

    private String cp_name;
    private int cp_type;
    private int cp_value;
    private int cp_min_price;

    @CreationTimestamp
    private LocalDateTime cp_issue_date;

    private LocalDateTime cp_exp_date;

    private int cp_stat;
}