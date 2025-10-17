package kr.co.shoply.service;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.entity.CsFaq;
import kr.co.shoply.entity.CsNotice;
import kr.co.shoply.repository.CsFaqRepository;
import kr.co.shoply.repository.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsService {

    private final CsNoticeRepository csNoticeRepository;
    private final ModelMapper modelMapper;
    private final CsFaqRepository csFaqRepository;

    public List<CsNoticeDTO> getRecentNotices(int limit) {
        return csNoticeRepository.findTopN(limit)
                .stream()
                .map(entity -> CsNoticeDTO.builder()
                        .cs_no(entity.getCs_no())
                        .cs_title(entity.getCs_title())
                        .cs_rdate(entity.getCs_rdate().toString())
                        .build())
                .toList();
    }

    /* ---------------- NOTICE  ---------------- */
    /** 단일 공지 조회 */
    @Transactional(readOnly = true)
    public CsNoticeDTO getCsNotice(int cs_no) {
        return csNoticeRepository.findById(cs_no)
                .map(csNotice -> modelMapper.map(csNotice, CsNoticeDTO.class))
                .orElse(null);
    }

    /** 전체 공지 조회 */
    @Transactional(readOnly = true)
    public List<CsNoticeDTO> getCsNoticeAll() {
        return csNoticeRepository.findAll().stream()
                .map(csNotice -> modelMapper.map(csNotice, CsNoticeDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<CsNoticeDTO> getCsNoticePage(PageRequestDTO req) {
        Pageable pageable = PageRequest.of(Math.max(req.getPg() - 1, 0), req.getSize());

        String type = req.getCsType(); // cat1(=분류)이 여기로 들어오게 컨트롤러에서 세팅
        Page<CsNotice> page = csNoticeRepository.findPage(type, pageable);

        List<CsNoticeDTO> dtoList = page.getContent().stream()
                .map(v -> modelMapper.map(v, CsNoticeDTO.class))
                .toList();

        return PageResponseDTO.<CsNoticeDTO>builder()
                .pageRequestDTO(req)
                .dtoList(dtoList)
                .total((int) page.getTotalElements())
                .build();
    }

    /* ---------------- FAQ ---------------- */

    // 전체
    public List<CsFaqDTO> getCsFaqAll() {
        List<CsFaq> list = csFaqRepository.findAll(); // ⬅ 정렬 제거
        return list.stream()
                .map(csFaq -> modelMapper.map(csFaq, CsFaqDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public CsFaqDTO getCsFaq(int csFaqNo) {
        CsFaq entity = csFaqRepository.findById(csFaqNo).orElse(null);
        if (entity == null) return null;
        return modelMapper.map(entity, CsFaqDTO.class);
    }

    // 카테고리(1차/2차) 필터 목록 (정렬 제거)
    public List<CsFaqDTO> getCsFaqList(String cat1, String cat2) {
        List<CsFaq> list;

        if (has(cat1) && has(cat2)) {
            // 엔티티가 스네이크이므로 리포지토리의 @Query 메서드 사용
            list = csFaqRepository.findByCate1AndCate2(cat1, cat2);
        } else if (has(cat1)) {
            list = csFaqRepository.findByCate1(cat1);
        } else {
            list = csFaqRepository.findAll();
        }

        return list.stream()
                .map(v -> modelMapper.map(v, CsFaqDTO.class))
                .toList();
    }

    // 사이드바용 1차/2차 카테고리
    public List<String> getCat1List() {
        return csFaqRepository.findAllCat1().stream()
                .filter(this::has).distinct().sorted().toList();
    }

    public List<String> getCat2List(String cat1) {
        if (!has(cat1)) return List.of();
        return csFaqRepository.findCat2ByCat1(cat1).stream()
                .filter(this::has).distinct().sorted().toList();
    }

    private boolean has(String s){ return s != null && !s.isBlank(); }
}