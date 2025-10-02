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
    private String ord_No;
    private String mem_Id;
    private String ord_Name;
    private String ord_Hp;
    private String ord_Zip;
    private String ord_Addr1;
    private String ord_Addr2;
    private Integer ord_Total;
    private Date ord_Date;
    private String ord_Payment;
}
