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


@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    /* 등록 */
    public Integer register(RecruitDTO dto) {
        Recruit entity = dtoToEntity(dto);
        recruitRepository.save(entity);
        return entity.getRec_no();
    }

    /* 목록 */
    @Transactional(readOnly = true)
    public PageResponseDTO<RecruitDTO> list(PageRequestDTO req) {
        int page = Math.max(1, req.getPg());
        int size = req.getSize() > 0 ? req.getSize() : 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        String type = req.getSearchType();
        String keyword = normalize(req.getKeyword());

        Page<Recruit> pageData;

        if ("n".equalsIgnoreCase(type) && isNumber(keyword)) {
            pageData = recruitRepository.findPageByRec_no(Integer.parseInt(keyword), pageable);

        } else if ("t".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            pageData = recruitRepository.searchByTitle(keyword, pageable);

        } else if ("d".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            pageData = recruitRepository.searchByDepartment(keyword, pageable);

        } else if ("e".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            pageData = recruitRepository.searchByExperience(keyword, pageable);

        } else if ("j".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            pageData = recruitRepository.searchByJobType(keyword, pageable);

        } else if ("s".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            pageData = recruitRepository.searchByStatus(keyword, pageable);

        } else {
            pageData = recruitRepository.fetchAll(pageable); // ← findAll() 대신 @Query 버전
        }

        List<RecruitDTO> list = pageData.map(this::entityToDto).getContent();
        int total = (int) pageData.getTotalElements();

        return PageResponseDTO.<RecruitDTO>builder()
                .pageRequestDTO(req)
                .dtoList(list)
                .total(total)
                .build();
    }

    /* 단건 조회 */
    @Transactional(readOnly = true)
    public RecruitDTO read(Integer recNo) {
        Recruit rec = recruitRepository.findById(recNo)
                .orElseThrow(() -> new IllegalArgumentException("채용 공고를 찾을 수 없습니다. rec_no=" + recNo));
        return entityToDto(rec);
    }

    /* 수정 */
    public void modify(RecruitDTO dto) {
        Recruit rec = recruitRepository.findById(dto.getRec_no())
                .orElseThrow(() -> new IllegalArgumentException("채용 공고를 찾을 수 없습니다. rec_no=" + dto.getRec_no()));

        rec.setRec_title(dto.getRec_title());
        rec.setRec_department(dto.getRec_department());
        rec.setRec_experience(dto.getRec_experience());
        rec.setRec_job_type(dto.getRec_job_type());
        rec.setRec_status(dto.getRec_status());
        rec.setRec_content(dto.getRec_content());
        rec.setRec_qualification(dto.getRec_qualification());
        rec.setRec_welfare(dto.getRec_welfare());
        rec.setRec_start_time(dto.getRec_start_time());
        rec.setRec_end_date(dto.getRec_end_date());
    }

    /* 삭제 */
    public void remove(Integer recNo) {
        recruitRepository.deleteById(recNo);
    }

    public void removeBulk(List<Integer> ids) {
        recruitRepository.deleteAllById(ids);
    }

    /* === 내부 유틸 === */
    private String normalize(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }
    private boolean isNumber(String s) { return s != null && s.matches("\\d+"); }

    private Recruit dtoToEntity(RecruitDTO dto) {
        Recruit r = new Recruit();
        r.setRec_title(dto.getRec_title());
        r.setRec_department(dto.getRec_department());
        r.setRec_experience(dto.getRec_experience());
        r.setRec_job_type(dto.getRec_job_type());
        r.setRec_status(dto.getRec_status());
        r.setRec_content(dto.getRec_content());
        r.setRec_qualification(dto.getRec_qualification());
        r.setRec_welfare(dto.getRec_welfare());
        r.setRec_start_time(dto.getRec_start_time());
        r.setRec_end_date(dto.getRec_end_date());
        return r;
    }

    private RecruitDTO entityToDto(Recruit rec) {
        RecruitDTO dto = new RecruitDTO();
        dto.setRec_no(rec.getRec_no());
        dto.setRec_title(rec.getRec_title());
        dto.setRec_department(rec.getRec_department());
        dto.setRec_experience(rec.getRec_experience());
        dto.setRec_job_type(rec.getRec_job_type());
        dto.setRec_status(rec.getRec_status());
        dto.setRec_content(rec.getRec_content());
        dto.setRec_qualification(rec.getRec_qualification());
        dto.setRec_welfare(rec.getRec_welfare());
        dto.setRec_start_time(rec.getRec_start_time());
        dto.setRec_end_date(rec.getRec_end_date());
        if (rec.getRec_rdate() != null) dto.setRec_rdate(rec.getRec_rdate().toString());
        return dto;
    }
}