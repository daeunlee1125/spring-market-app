package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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
    private Long item_no;
    private String ord_no;
    private String prod_no;
    private String item_name;
    private Integer item_cnt;
    private Integer item_stat;
    private String item_delv;
    private String item_trk_num;
}