package kr.co.shoply.mapper;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemSellerMapper {
    public void insertMemSeller(MemSellerDTO memSellerDTO);
    public void insertMember(MemSellerDTO memSellerDTO);
    public List<MemSellerDTO> getSellers2();
    public List<MemSellerDTO> getSellerPage2(PageRequestDTO pageRequestDTO);
    public int selectCountTotal2(PageRequestDTO pageRequestDTO);
    public void updateStat2(MemberDTO memberDTO);
}
