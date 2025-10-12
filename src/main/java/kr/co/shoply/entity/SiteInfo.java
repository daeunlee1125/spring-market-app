package kr.co.shoply.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SITE_INFO")
public class SiteInfo {
    @Id
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
}
