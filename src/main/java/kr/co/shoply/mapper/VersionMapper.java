package kr.co.shoply.mapper;

import kr.co.shoply.dto.VersionDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VersionMapper {
    public VersionDTO selectRecentOne();
}
