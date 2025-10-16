package kr.co.shoply.service;

import kr.co.shoply.dto.QnaDTO;
import kr.co.shoply.entity.Qna;
import kr.co.shoply.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;

    /** 폼/사이드바에서 사용할 마스터 카테고리 */
    private static final Map<String, List<String>> CAT2_MASTER =
            new LinkedHashMap<>() {{
                put("회원", List.of("가입","탈퇴","회원정보","로그인"));
                put("쿠폰/혜택/이벤트", List.of("쿠폰/할인혜택","포인트","제휴","이벤트"));
                put("주문/결제", List.of("상품","결제","구매내역","영수증/증빙"));
                put("배송", List.of("배송상태/기간","배송정보확인/변경","해외배송","당일배송","해외직구"));
                put("취소/반품/교환", List.of("반품신청/철회","반품정보확인/변경","교환 AS신청/철회","교환정보확인/변경","취소신청/철회","취소확인/환불정보"));
                put("여행/숙박/항공", List.of("여행/숙박","항공"));
                put("안전거래", List.of("서비스 이용규칙 위반","지식재산권침해","법령 및 정책위반 상품","게시물 정책위반","직거래/외부거래유도","표시광고","청소년 위해상품/이미지"));
            }};

    public List<String> getCat1Master() {
        return new ArrayList<>(CAT2_MASTER.keySet());
    }
    public List<String> getCat2Master(String cat1) {
        if (cat1 == null || cat1.isBlank()) return List.of();
        return CAT2_MASTER.getOrDefault(cat1, List.of());
    }

    private boolean has(String s){ return s != null && !s.isBlank(); }

    /* 목록 (정렬은 레포지토리 JPQL ORDER BY 사용) */
    public List<QnaDTO> getQnaList(String cat1, String cat2){
        var list = qnaRepository.findList(has(cat1) ? cat1 : null,
                has(cat2) ? cat2 : null);
        return list.stream()
                .map(v -> modelMapper.map(v, QnaDTO.class))
                .toList();
    }

    /* 단건 */
    @Transactional(readOnly = true)
    public QnaDTO getQna(int q_no){
        return qnaRepository.findById(q_no)
                .map(v -> modelMapper.map(v, QnaDTO.class))
                .orElse(null);
    }

    /* 등록 */
    @Transactional
    public int register(QnaDTO dto){
        Qna entity = Qna.builder()
                .mem_id(dto.getMem_id())
                .q_cate1(dto.getQ_cate1())
                .q_cate2(dto.getQ_cate2())
                .q_channel(dto.getQ_channel())
                .q_title(dto.getQ_title())
                .q_content(dto.getQ_content())
                .q_reply(dto.getQ_reply())
                .q_comment(dto.getQ_comment())
                .build();
        return qnaRepository.save(entity).getQ_no();
    }

    /* 사이드바용 카테고리 (DB 기준) */
    public List<String> getCat1List(){
        return qnaRepository.findAllCat1();
    }

    public List<String> getCat2List(String cat1){
        if (!has(cat1)) return List.of();
        return qnaRepository.findCat2ByCat1(cat1);
    }
}
