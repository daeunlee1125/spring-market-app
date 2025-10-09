package kr.co.shoply.mapper;

import kr.co.shoply.dto.VersionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VersionMapper {
    public VersionDTO selectRecentOne();
    public void save(VersionDTO versionDTO);
    public List<VersionDTO> findAllOrdered();
}
