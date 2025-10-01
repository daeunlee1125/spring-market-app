package kr.co.shoply.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouopnDTO {
    private String cpNo;
    private String cpCode;
    private String memId;
    private Date cpIssuedDate;
    private Date cpUsedDate;
    private Integer cpStat;
}
