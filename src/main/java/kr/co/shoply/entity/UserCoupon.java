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
@Table(name = "USER_COUPON")
public class UserCoupon {

    @Id
    private String cp_no;


    private String cp_code;
    private String mem_id;

    @CreationTimestamp
    private LocalDateTime cp_issued_date;

    private LocalDateTime cp_used_date;


    private int cp_stat;
}