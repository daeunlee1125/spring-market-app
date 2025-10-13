package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date; // LocalDateTime 대신 java.util.Date를 import

@Getter
@Setter // MyService에서 setter를 사용하려면 필요합니다.
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    private String ord_no;
    private String mem_id;
    private String ord_name;
    private String ord_hp;
    private String ord_zip;
    private String ord_addr1;
    private String ord_addr2;
    private Integer ord_total;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP) // Date 타입에 맞게 TemporalType 설정
    private Date ord_date; // LocalDateTime -> Date로 변경

    private String ord_payment;
}