package kr.co.shoply.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysCouponDTO {
    private String cpCode;
    private String cpName;
    private Integer cpType;
    private Integer cpValue;
    private Integer cpMinPrice;
    private Date cpIssueDate;
    private Date cpExpDate;
}
