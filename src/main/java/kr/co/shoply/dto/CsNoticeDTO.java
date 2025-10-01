package kr.co.practice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsNoticeDTO {

    private int cs_no;
    private String cs_type;
    private String cs_title;
    private String cs_content;
    private int cs_hit;
    private String cs_rdate;
}
