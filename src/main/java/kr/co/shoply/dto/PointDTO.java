package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDTO {
    private int p_no;
    private String mem_id;
    private int p_type;
    private int p_point;
    private String p_info;
    private String p_date;
    private String p_exp_date;

}
