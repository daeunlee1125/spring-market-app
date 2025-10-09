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
    private Date ord_date;
    private String ord_payment;
}
