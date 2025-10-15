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


    // admin/shop/sales 추가 필드
    private int ord_cnt;
    private int ord_st_cnt;
    private int stat2_cnt;
    private int stat3_cnt;
    private int stat4_cnt;
    private int tot_ord_price;
    private int tot_sell_price;

    private String range;


}
