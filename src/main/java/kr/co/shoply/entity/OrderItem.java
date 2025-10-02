package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemNo;

    private String ord_No;

    private String prod_No;

    private String item_Name;

    private Integer item_Cnt;

    private Integer item_Stat;
}