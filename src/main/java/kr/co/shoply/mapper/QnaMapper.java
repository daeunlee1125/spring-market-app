package kr.co.shoply.mapper;

import kr.co.shoply.dto.AdInfoDTO;
import kr.co.shoply.dto.QnaDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QnaMapper {
    public void insertQna(QnaDTO qnaDTO);
    // 필요에 따라 조회, 수정, 삭제 메서드 추가 가능
    public AdInfoDTO qnaCnt2();
    public List<QnaDTO> selectQna2();
}