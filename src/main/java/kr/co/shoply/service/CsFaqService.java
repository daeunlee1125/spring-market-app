package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.entity.CsFaq;
import kr.co.shoply.mapper.CsFaqMapper;
import kr.co.shoply.repository.CsFaqRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CsFaqService {

    private final CsFaqMapper csFaqMapper;
    private final CsFaqRepository csFaqRepository;
    private final ModelMapper modelMapper;

    public List<CsFaqDTO> selectFaqList(String cate1, String cate2) {
        // cate1, cate2 둘 다 비어 있으면 전체 목록 조회
        if ((cate1 == null || cate1.isEmpty()) && (cate2 == null || cate2.isEmpty())) {
            return csFaqMapper.selectAllFaqs();
        }

        // cate1만 선택된 경우
        if (cate1 != null && !cate1.isEmpty() && (cate2 == null || cate2.isEmpty())) {
            return csFaqMapper.selectFaqsByCate1(cate1);
        }

        // cate1 + cate2 모두 선택된 경우
        return csFaqMapper.selectFaqsByCate1AndCate2(cate1, cate2);
    }


    // 단일 삭제
    public void deleteFaq(int cs_faq_no) {
        csFaqRepository.deleteById(cs_faq_no);
    }

    // 다중 삭제
    public void deleteFaqs(List<Integer> ids) {
        csFaqRepository.deleteAllByIdInBatch(ids);
    }


    // 글보기

    public CsFaqDTO findById(int cs_faq_no) {
        return csFaqRepository.findById(cs_faq_no)
                .map(f -> CsFaqDTO.builder()
                        .cs_faq_no(f.getCs_faq_no())
                        .cs_faq_cate1(f.getCs_faq_cate1())
                        .cs_faq_cate2(f.getCs_faq_cate2())
                        .cs_faq_title(f.getCs_faq_title())
                        .cs_faq_content(f.getCs_faq_content())
                        .cs_faq_hit(f.getCs_faq_hit())
                        .cs_faq_rdate(f.getCs_faq_rdate().toString())
                        .build())
                .orElse(null);
    }

    // 조회수

    @Transactional
    public void increaseHit(int cs_faq_no) {
        csFaqRepository.increaseHit(cs_faq_no);
    }


    public void updateFaq(CsFaqDTO faq) {
        CsFaq entity = csFaqRepository.findById(faq.getCs_faq_no())
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found"));

        entity.setCs_faq_cate1(faq.getCs_faq_cate1());
        entity.setCs_faq_cate2(faq.getCs_faq_cate2());
        entity.setCs_faq_title(faq.getCs_faq_title());
        entity.setCs_faq_content(faq.getCs_faq_content());

        csFaqRepository.save(entity);
    }

    @Transactional
    public void saveFaq(CsFaqDTO dto) {
        CsFaq faq = CsFaq.builder()
                .cs_faq_cate1(dto.getCs_faq_cate1())
                .cs_faq_cate2(dto.getCs_faq_cate2())
                .cs_faq_title(dto.getCs_faq_title())
                .cs_faq_content(dto.getCs_faq_content())
                .cs_faq_hit(0)
                .build();

        csFaqRepository.save(faq);
    }









}
