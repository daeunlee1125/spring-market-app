package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String cp_no;


    private String cp_code;
    private String mem_id;
    private String cp_issued_date;
    private String cp_used_date;
    private int cp_stat;
    private LocalDate cp_exp_date;



    private String cp_issuer_name;
    private String cp_note;

}
