package kr.co.shoply.service;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.RecruitDTO;
import kr.co.shoply.entity.Recruit;
import kr.co.shoply.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyRecruitService {

    private final RecruitRepository recruitRepository;

    public PageResponseDTO<RecruitDTO> list(PageRequestDTO req) {
        int page = Math.max(1, req.getPg());
        int size = req.getSize() > 0 ? req.getSize() : 10;

        // ✅ 정렬 없이 페이지 객체만 생성 (언더스코어 정렬키 안 씀)
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Recruit> pageData = recruitRepository.fetchAll(pageable); // ✅ JPQL에서 정렬

        List<RecruitDTO> list = pageData.getContent().stream()
                .map(this::entityToDto)
                .toList();

        return PageResponseDTO.<RecruitDTO>builder()
                .pageRequestDTO(req)
                .dtoList(list)
                .total((int) pageData.getTotalElements())
                .build();
    }

    public RecruitDTO read(Integer recNo) {
        Recruit r = recruitRepository.findOneByRecNo(recNo)
                .orElseThrow(() -> new NoSuchElementException("공고가 없습니다."));
        return entityToDto(r);
    }

    private RecruitDTO entityToDto(Recruit r) {
        RecruitDTO d = new RecruitDTO();
        d.setRec_no(r.getRec_no());
        d.setRec_title(r.getRec_title());
        d.setRec_department(r.getRec_department());
        d.setRec_experience(r.getRec_experience());
        d.setRec_job_type(r.getRec_job_type());
        d.setRec_status(r.getRec_status());
        d.setRec_content(r.getRec_content());
        d.setRec_qualification(r.getRec_qualification());
        d.setRec_welfare(r.getRec_welfare());
        if (r.getRec_start_time()!=null) d.setRec_start_time(r.getRec_start_time().toString());
        if (r.getRec_end_date()!=null)   d.setRec_end_date(r.getRec_end_date().toString());
        if (r.getRec_rdate()!=null)      d.setRec_rdate(r.getRec_rdate().toString());
        return d;
    }
}

