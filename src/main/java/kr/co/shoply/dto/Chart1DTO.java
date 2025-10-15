package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chart1DTO {

    private String ch_date;
    private int order_cnt;
    private int pay_cnt;
    private int cancel_cnt;
}
