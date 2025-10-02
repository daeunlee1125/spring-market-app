package kr.co.shoply.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`order`") // Oracle 예약어 처리
public class Order {
    @Id

    private String ord_No;

    private String mem_Id;

    private String ord_Name;

    private String ord_Hp;

    private String ord_Zip;

    private String ord_Addr1;

    private String ord_Addr2;

    private Integer ord_Total;

    @CreationTimestamp // 객체 생성 시 현재 시간 자동 입력
    private LocalDateTime ord_Date;

    private String ord_Payment;
}