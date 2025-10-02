package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermsDTO {

    private int t_no;
    private String t_terms;
    private String t_tax;
    private String t_finance;
    private String t_privacy;
    private String t_location;
}
