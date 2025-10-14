package kr.co.shoply.mapper;


import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CsNoticeMapper {

    // 공지사항 목록 조회
    List<CsNoticeDTO> selectNoticeList(PageRequestDTO pageRequestDTO);

    // 전체 공지사항 수 조회
    int selectNoticeTotal(PageRequestDTO pageRequestDTO);



    // 글 삭제
    public void deleteNotices(@Param("ids") List<Integer> ids);

    
    // 글 등록
    void insertNotice(CsNoticeDTO dto);


    // 조회수
    void updateHit(int csNo);


    CsNoticeDTO selectNotice(int csNo);
    void updateNotice(CsNoticeDTO dto);

    public List<CsNoticeDTO> noticeList2();


}
