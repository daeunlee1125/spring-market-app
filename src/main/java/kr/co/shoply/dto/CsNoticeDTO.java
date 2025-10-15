package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsNoticeDTO {

    private int csNo;
    private String csType;
    private String csTitle;
    private String csContent;
    private int csHit;
    private String csRdate;

    public String getCs_rdate() {
        return csRdate.substring(2, 10).replace("T", " ");
    }
}
