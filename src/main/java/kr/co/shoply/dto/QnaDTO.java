package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaDTO {

    private int qNo;
    private String memId;
    private String qCate1;
    private String qCate2;
    private String qChannel;
    private String qTitle;
    private String qContent;

    private String qrDate;
    public String getQrDate() { return qrDate; }
    private String qReply;
    private String qComment;

}
