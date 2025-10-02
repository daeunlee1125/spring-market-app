package kr.co.shoply.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private String mem_id;
    private String mem_pass;
    private String mem_name;
    private String mem_gen;
    private String mem_hp;
    private String mem_email;
    private int mem_level;
    private String mem_stat;
    private String mem_rdate;
    private String mem_lastLogin;
    private String mem_zip;
    private String mem_addr1;
    private String mem_addr2;
    private String mem_rank;
}
