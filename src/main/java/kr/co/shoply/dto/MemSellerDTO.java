package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemSellerDTO {
    private String mem_id;
    private String corp_name;
    private String corp_reg_hp;
    private String corp_tel_hp;
    private String corp_fax;

    private String mem_pass;
    private String mem_name;
    private String mem_hp;
    private int mem_level;
    private String mem_rank;
    private String mem_stat;
    private String mem_rdate;
    private String mem_zip;
    private String mem_addr1;
    private String mem_addr2;

}
