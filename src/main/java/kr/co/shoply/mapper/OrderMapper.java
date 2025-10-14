package kr.co.shoply.mapper;

import kr.co.shoply.dto.AdInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    public AdInfoDTO ordStatCnt2();
}
