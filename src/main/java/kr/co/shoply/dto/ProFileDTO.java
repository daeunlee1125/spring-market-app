package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProFileDTO {
    private int f_no;
    private int prod_no;
    private String f_name;
    private String f_rdate;
}
