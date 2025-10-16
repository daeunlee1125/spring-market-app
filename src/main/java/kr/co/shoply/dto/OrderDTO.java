package kr.co.shoply.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private String ord_no;
    private String mem_id;
    private String ord_name;
    private String ord_hp;
    private String ord_zip;
    private String ord_addr1;
    private String ord_addr2;
    private Integer ord_total;
    private String ord_date;
    private String ord_payment;
    private int ord_stat;
    // 추가 필드
    private String mem_name;
    private int item_cnt;
    private String prod_no;
    private String prod_name;
    private int prod_price;
    private int prod_sale;
    private int prod_deliv_price;
    private int item_no;
    private String corp_name;
    private String f_name;
}
