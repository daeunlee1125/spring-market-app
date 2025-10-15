package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsFaqDTO {

    private int csFaqNo;
    private String csFaqCate1;
    private String csFaqCate2;
    private String csFaqTitle;
    private String csFaqContent;
    private int csFaqHit;
    private String csFaqRdate;

    public String getCsFaqRdateShort() {
        if (csFaqRdate == null || csFaqRdate.length() < 10) return csFaqRdate;
        return csFaqRdate.substring(2, 10).replace("T", " ");
    }

}
