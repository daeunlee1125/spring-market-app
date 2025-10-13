package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteInfoDTO {
    private String s_title;
    private String s_subtitle;
    private String s_logo1;
    private String s_logo2;
    private String s_logo3;
    private String s_corp_name;
    private String s_ceo;
    private String s_reg_hp;
    private String s_tel_reg;
    private String s_zip;
    private String s_addr1;
    private String s_addr2;
    private String s_cs_hp;
    private String s_cs_time;
    private String s_cs_email;
    private String s_cs_hp2;
    private String s_copy;
    private int s_no;

}
