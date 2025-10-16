package kr.co.shoply.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private int rev_no;
    private String prod_no;
    private String mem_id;
    private String prodName;
    private String rev_content;
    private int rev_rating;
    private Date rev_rdate;

    // 기존 단일 이미지 필드 유지 (기존 DB 컬럼 연동용)
    private String rev_img_path;

    // 추가 필드
    private String privateMemId; // 화면에 출력할 때 abc123 -> ab****로 출력하기 위한 용도

    // 추가: 업로드 파일 여러 개 처리용
    private List<String> rev_files; // 업로드 파일명을 담는 리스트
}

