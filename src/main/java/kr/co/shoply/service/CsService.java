package kr.co.shoply.service;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.entity.CsFaq;
import kr.co.shoply.entity.CsNotice;
import kr.co.shoply.repository.CsFaqRepository;
import kr.co.shoply.repository.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    /** 카테고리별 공지 목록 */
    @Transactional(readOnly = true)
    public List<CsNoticeDTO> getCsNoticeList(String cat1) {
        List<CsNotice> list;
        if (cat1 == null || cat1.isBlank()) {
            // 전체
            list = csNoticeRepository.findList(null);
        } else {
            // 특정 카테고리
            list = csNoticeRepository.findList(cat1);
        }
        return list.stream()
                .map(v -> modelMapper.map(v, CsNoticeDTO.class))
                .toList();
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