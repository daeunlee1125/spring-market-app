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
}
