package kr.co.shoply.service;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.entity.CsFaq;
import kr.co.shoply.entity.CsNotice;
import kr.co.shoply.repository.CsFaqRepository;
import kr.co.shoply.repository.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
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

    public CsNoticeDTO getCsNotice(int cs_no) {

        Optional<CsNotice> optCsNotice = csNoticeRepository.findById(cs_no);

        if (optCsNotice.isPresent()) {
            CsNotice csNotice = optCsNotice.get();
            return modelMapper.map(csNotice, CsNoticeDTO.class);
        }
        return null;
    }

    public List<CsNoticeDTO> getCsNoticeAll(){
        List<CsNotice> list = csNoticeRepository.findAll();
        return list.stream()
                .map(csNotice -> modelMapper.map(csNotice, CsNoticeDTO.class))
                .toList();
    }

    /** 공지 정렬 규칙 */
    private Sort noticeSort() {
        return Sort.by(Sort.Order.desc("csRdate"))
                .and(Sort.by(Sort.Order.desc("csNo")));
    }

    @Transactional(readOnly = true)
    public List<CsNoticeDTO> getCsNoticeList(String cat1){
        List<CsNotice> list = (cat1 == null || cat1.isBlank())
                ? csNoticeRepository.findAll(noticeSort())
                : csNoticeRepository.findByCsType(cat1, noticeSort());
        return list.stream().map(v -> modelMapper.map(v, CsNoticeDTO.class)).toList();
    }

    public List<CsFaqDTO> getCsFaqAll(){
        List<CsFaq> list = csFaqRepository.findAll(defaultSort());
        return list.stream()
                .map(csFap -> modelMapper.map(csFap, CsFaqDTO.class))
                .toList();
    }
    @Transactional
    public CsFaqDTO getCsFaq(int csFaqNo) {
        CsFaq entity = csFaqRepository.findById(csFaqNo).orElse(null);
        if (entity == null) return null;

        return modelMapper.map(entity, CsFaqDTO.class);
    }

        /** 정렬 규칙: sortOrder ASC, 그 다음 id ASC (엔티티 필드명에 맞춰 수정 가능) */
        private Sort defaultSort() {
            return Sort.by(Sort.Order.desc("csFaqRdate"))
                    .and(Sort.by(Sort.Order.desc("csFaqNo")));
        }

    /** 카테고리(1차/2차)로 필터된 FAQ 목록 */
    public List<CsFaqDTO> getCsFaqList(String cat1, String cat2){
        Sort sort = defaultSort();
        List<CsFaq> list;

        if (has(cat1) && has(cat2)) {
            list = csFaqRepository.findByCsFaqCate1AndCsFaqCate2(cat1, cat2, sort);
        } else if (has(cat1)) {
            list = csFaqRepository.findByCsFaqCate1(cat1, sort);
        } else {
            list = csFaqRepository.findAll(sort);
        }

        return list.stream()
                .map(v -> modelMapper.map(v, CsFaqDTO.class))
                .toList();
    }

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