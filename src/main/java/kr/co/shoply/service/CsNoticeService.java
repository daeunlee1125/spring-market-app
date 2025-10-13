package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.mapper.CsNoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CsNoticeService {

    private final CsNoticeMapper csNoticeMapper;

    public PageResponseDTO<CsNoticeDTO> selectNoticeList(PageRequestDTO pageRequestDTO) {

        // 목록 데이터
        List<CsNoticeDTO> list = csNoticeMapper.selectNoticeList(pageRequestDTO);

        // 전체 개수
        int total = csNoticeMapper.selectNoticeTotal(pageRequestDTO);

        // 응답 DTO 조립 (공통 PageResponseDTO 사용)
        return PageResponseDTO.<CsNoticeDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    @Transactional
    public void deleteNotices(List<Integer> ids) {
        csNoticeMapper.deleteNotices(ids);
    }


    // 공지사항 등록
    public void insertNotice(CsNoticeDTO dto) {
        csNoticeMapper.insertNotice(dto);
    }


    // 글 수정

    public CsNoticeDTO getNotice(int csNo) {
        return csNoticeMapper.selectNotice(csNo);
    }

    public void updateNotice(CsNoticeDTO dto) {
        csNoticeMapper.updateNotice(dto);
    }

    public void incrementHit(int csNo) {
        csNoticeMapper.updateHit(csNo);
    }


}
