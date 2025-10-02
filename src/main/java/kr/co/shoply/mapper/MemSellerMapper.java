package kr.co.shoply.mapper;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemSellerMapper {
    public void insertMemSeller(MemSellerDTO memSellerDTO);
    public void insertMember(MemSellerDTO memSellerDTO);
}
