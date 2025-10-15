package kr.co.shoply.service;

import kr.co.shoply.dto.QnaDTO;
import kr.co.shoply.entity.Qna;
import kr.co.shoply.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QnaService {

private final QnaRepository qnaRepository;
private final ModelMapper modelMapper;

private static final Map<String, List<String>> CAT2_MASTER =
        new java.util.LinkedHashMap<>() {{
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

    private Sort defaultSort() {
        return Sort.by(Sort.Order.desc("qrDate"))
                .and(Sort.by(Sort.Order.desc("qNo")));
    }

    /* 목록 */
    public List<QnaDTO> getQnaList(String cat1, String cat2){
        var list = qnaRepository.findList(has(cat1)?cat1:null, has(cat2)?cat2:null, defaultSort());
        return list.stream().map(v -> modelMapper.map(v, QnaDTO.class)).toList();
    }

    /* 단건 */
    @Transactional(readOnly = true)
    public QnaDTO getQna(int qNo){
        return qnaRepository.findById(qNo)
                .map(v -> modelMapper.map(v, QnaDTO.class))
                .orElse(null);
    }

    /* 등록 */
    @Transactional
    public int register(QnaDTO dto){
        Qna entity = Qna.builder()
                .memId(dto.getMemId())
                .qCate1(dto.getQCate1())
                .qCate2(dto.getQCate2())
                .qChannel(dto.getQChannel())
                .qTitle(dto.getQTitle())
                .qContent(dto.getQContent())
                .qReply(dto.getQReply())
                .qComment(dto.getQComment())
                .build();
        return qnaRepository.save(entity).getQNo();
    }

    /* 카테고리(사이드바용) */
    public List<String> getCat1List(){
        return qnaRepository.findAllCat1().stream()
                .filter(this::has).distinct().sorted().toList();
    }

    public List<String> getCat2List(String cat1){
        if (!has(cat1)) return List.of();
        return qnaRepository.findCat2ByCat1(cat1).stream()
                .filter(this::has).distinct().sorted().toList();
    }
}
