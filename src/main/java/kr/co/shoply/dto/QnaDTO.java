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
    private String q_reply;
    private String q_comment;

}
