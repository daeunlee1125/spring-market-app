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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @SequenceGenerator(name = "order_item_seq", sequenceName = "ORDER_ITEM_ITEM_NO_SEQ", allocationSize = 1)
    @Column(name = "item_no")
    private Long itemNo;

    @Column(name = "ord_no", length = 11, nullable = false)
    private String ordNo;

    @Column(name = "prod_no", length = 9, nullable = false)
    private String prodNo;

    @Column(name = "item_name", length = 30)
    private String itemName;

    @Column(name = "item_cnt")
    private Integer itemCnt;

    @Column(name = "item_stat")
    private Integer itemStat;
}