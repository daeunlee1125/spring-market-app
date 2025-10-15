package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaDTO {

    private int q_no;
    private String mem_id;
    private String q_cate1;
    private String q_cate2;
    private String q_channel;
    private String q_title;
    private String q_content;
    private String q_rdate;

    public String getQ_rdate() {
        return q_rdate.substring(2, 10).replace("T", " ");
    }

    private String q_reply;
    private String q_comment;

    // 추가 필드
    private String masked_id;

}
