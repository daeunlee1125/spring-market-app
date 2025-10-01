package kr.co.practice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsFaqDTO {

    private int cs_faq_no;
    private String cs_faq_cate1;
    private String cs_faq_cate2;
    private String cs_faq_title;
    private String cs_faq_content;
    private int cs_faq_hit;
    private String cs_faq_rdate;

}
