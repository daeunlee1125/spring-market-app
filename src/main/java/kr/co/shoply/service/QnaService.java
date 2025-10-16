package kr.co.shoply.service;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.QnaAnswerRequestDTO;
import kr.co.shoply.dto.QnaDTO;
import kr.co.shoply.entity.Qna;
import kr.co.shoply.mapper.QnaMapper;
import kr.co.shoply.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;
    private final QnaMapper qnaMapper;

    @Transactional(readOnly = true)
    public List<QnaDTO> getRecentQna(int limit) {
        return qnaRepository.findTopN(limit).stream()
                .map(e -> {
                    QnaDTO d = new QnaDTO();
                    d.setQ_no(e.getQ_no());
                    d.setMem_id(e.getMem_id());
                    d.setQ_cate1(e.getQ_cate1());
                    d.setQ_cate2(e.getQ_cate2());
                    d.setQ_channel(e.getQ_channel());
                    d.setQ_title(e.getQ_title());
                    d.setQ_content(e.getQ_content());
                    d.setQ_reply(e.getQ_reply());
                    d.setQ_comment(e.getQ_comment());
                    // ★ 포인트: DTO의 q_rdate는 String 이어야 하며, 여기서 문자열로 변환
                    d.setQ_rdate(e.getQ_rdate() == null ? null : e.getQ_rdate().toString());
                    return d;
                })
                .toList();
    }
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

    @Transactional(readOnly = true)
    public PageResponseDTO<QnaDTO> getQnaPage(PageRequestDTO req, String cat1, String cat2) {

        Pageable pageable = PageRequest.of(Math.max(req.getPg() - 1, 0), req.getSize());

        Page<Qna> page = qnaRepository.findPage(
                (cat1 == null || cat1.isBlank()) ? null : cat1,
                (cat2 == null || cat2.isBlank()) ? null : cat2,
                pageable
        );

        List<QnaDTO> dtoList = page.getContent().stream()
                .map(v -> modelMapper.map(v, QnaDTO.class))
                .toList();

        return PageResponseDTO.<QnaDTO>builder()
                .pageRequestDTO(req)
                .dtoList(dtoList)
                .total((int) page.getTotalElements())
                .build();
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

    public PageResponseDTO<QnaDTO> getQnaList3(int page, String cate1, String cate2) {

        // 1. 전달받은 파라미터로 PageRequestDTO 객체를 생성합니다.
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPg(page);
        pageRequestDTO.setCate1(cate1);
        pageRequestDTO.setCate2(cate2);
        // pageRequestDTO.setSize(10); // DTO에 @Builder.Default로 기본값이 10으로 설정되어 있으므로 생략 가능

        // 2. 조건에 맞는 전체 게시물 수를 가져옵니다.
        // (매퍼가 DTO를 파라미터로 받도록 수정하는 것이 좋음)
        int total = qnaMapper.selectQnaCnt3(pageRequestDTO);

        // 3. 페이징된 목록을 가져옵니다.
        // (매퍼가 DTO를 파라미터로 받으면 offset 계산도 DTO의 getOffset()으로 자동 처리됨)
        List<QnaDTO> dtoList = qnaMapper.selectQnaByCate3(pageRequestDTO);

        // 4. PageResponseDTO의 생성자에 pageRequestDTO를 전달하여 객체를 생성하고 반환합니다.
        // 이제 생성자 형식이 일치하므로 에러가 발생하지 않습니다.
        return new PageResponseDTO<>(pageRequestDTO, dtoList, total);
    }

    public QnaDTO getQna3(int q_no) {
        return qnaMapper.selectQna3(q_no);
    }

    public void modifyAnswerQna3(QnaAnswerRequestDTO dto) {
        qnaMapper.updateQnaReply3(dto);
    }

    public void removeQnaList3(List<Integer> list){
        qnaMapper.deleteQnaList3(list);
    }


}
