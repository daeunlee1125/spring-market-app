package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdInfoDTO {
    // order
    private int st1;           // 결제대기 수
    private int st3;           // 주문취소 수
    private int ordNum;        // 전체 주문 수
    private int todOrdNum;     // 오늘 주문 수
    private int yesOrdNum;     // 어제 주문 수
    private int ordTotal;      // 전체 주문 금액 합계
    private int todOrdTotal;   // 오늘 주문 금액 합계
    private int yesOrdTotal;   // 어제 주문 금액 합계

    // member
    private int regTotal;      // 전체 회원 수
    private int todRegTotal;   // 오늘 가입 회원 수
    private int yesRegTotal;   // 어제 가입 회원 수

    // qna
    private int qnaTotal;      // 전체 문의 수
    private int todQnaTotal;   // 오늘 문의 수
    private int yesQnaTotal;   // 어제 문의 수

    // order_item
    private int st1Total;      // 배송준비 상태
    private int st5Total;      // 교환신청 상태
    private int st6Total;      // 반품신청 상태

    private int visit;
    private int todVisit;
    private int yesVisit;


}
