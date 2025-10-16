package kr.co.shoply.dto;

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

    public String getCsFaqRdateShort() {
        if (cs_faq_rdate == null || cs_faq_rdate.length() < 10) return cs_faq_rdate;
        return cs_faq_rdate.substring(2, 10).replace("T", " ");
    }

}
