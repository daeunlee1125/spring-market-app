package kr.co.shoply.mapper;


import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PointDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMemberMapper {

    // 회원 목록 조회 (페이징)
    List<MemberDTO> selectMemberList(PageRequestDTO pageRequestDTO);

    // 전체 회원 수
    int selectMemberTotal(PageRequestDTO pageRequestDTO);

    // 회원 상세 조회
    MemberDTO selectMemberById(String memId);


    int AdminupdateMember(MemberDTO memberDTO);


    // 회원 상태 변경
    void updateAdminMemberStatus(@Param("mem_id") String memId,
                                 @Param("mem_stat") String memStat);



    // 회원등급 수정
    void AdminupdateRank(@Param("mem_id") String memId,
                         @Param("mem_rank") String memRank);



    //  포인트 관리 추가
    List<PointDTO> selectAdminPointList(PageRequestDTO pageRequestDTO);
    int selectAdminTotalCount(PageRequestDTO pageRequestDTO);


    void deleteSelectedPoints(@Param("p_no") List<Integer> p_no);


}
