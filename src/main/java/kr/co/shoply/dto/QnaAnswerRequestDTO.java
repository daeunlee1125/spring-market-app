package kr.co.shoply.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaAnswerRequestDTO {
    @JsonProperty("qNo") // 👈 JSON의 "qNo" 키를 이 필드에 매핑하라고 직접 지정
    private int qNo;

    @JsonProperty("content") // 👈 일관성을 위해 여기도 추가해주는 것이 좋습니다.
    private String content;
}
