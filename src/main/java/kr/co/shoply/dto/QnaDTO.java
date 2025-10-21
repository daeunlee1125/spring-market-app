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

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getMaskedMemId() {
        if (mem_id == null) return null;

        int visibleLength = 3;
        int totalLength = mem_id.length();

        String visiblePart = mem_id.substring(0, Math.min(visibleLength, totalLength));
        StringBuilder masked = new StringBuilder(visiblePart);
        for (int i = visibleLength; i < totalLength; i++) {
            masked.append("*");
        }
        return masked.toString();
    }

    private String q_reply;
    private String q_comment;

    // 추가 필드
    private String masked_id;

}
