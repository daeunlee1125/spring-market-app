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
    private String ordNo;
    private String memId;
    private String ordName;
    private String ordHp;
    private String ordZip;
    private String ordAddr1;
    private String ordAddr2;
    private Integer ordTotal;
    private Date ordDate;
    private String ordPayment;
}
