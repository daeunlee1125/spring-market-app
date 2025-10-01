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
    @Column(name = "ord_no", length = 11)
    private String ordNo;

    @Column(name = "mem_id", length = 30, nullable = false)
    private String memId;

    @Column(name = "ord_name", length = 30)
    private String ordName;

    @Column(name = "ord_hp", length = 13)
    private String ordHp;

    @Column(name = "ord_zip", length = 30)
    private String ordZip;

    @Column(name = "ord_addr1", length = 50)
    private String ordAddr1;

    @Column(name = "ord_addr2", length = 50)
    private String ordAddr2;

    @Column(name = "ord_total")
    private Integer ordTotal;

    @CreationTimestamp // 객체 생성 시 현재 시간 자동 입력
    @Column(name = "ord_date", nullable = false)
    private LocalDateTime ordDate;

    @Column(name = "ord_payment", length = 50)
    private String ordPayment;
}