package kr.co.shoply.mapper;

import kr.co.shoply.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {
    public void insertQna(QnaDTO qnaDTO);
    // 필요에 따라 조회, 수정, 삭제 메서드 추가 가능
    public AdInfoDTO qnaCnt2();
    public List<QnaDTO> selectQna2();
    List<QnaDTO> selectQnaAll3(String cate1, String cate2);
    // 여러 파라미터 대신 DTO 하나만 받도록 변경
    int selectQnaCnt3(PageRequestDTO pageRequestDTO);
    List<QnaDTO> selectQnaByCate3(PageRequestDTO pageRequestDTO);
    QnaDTO selectQna3(int q_no);
    void updateQnaReply3(QnaAnswerRequestDTO qnaDTO);


    void deleteQnaList3(@Param("list") List<Integer> list);
}