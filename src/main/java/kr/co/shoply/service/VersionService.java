package kr.co.shoply.service;

import kr.co.shoply.dto.CopyrightDTO;
import kr.co.shoply.dto.VersionDTO;
import kr.co.shoply.entity.Version;
import kr.co.shoply.mapper.VersionMapper;
import kr.co.shoply.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VersionService {
    private final VersionMapper versionMapper;
    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;

    public VersionDTO getVersion() {
        VersionDTO versionDTO = versionMapper.selectRecentOne();
        return versionDTO;
    }

    public List<VersionDTO> getVersions() {
        List<VersionDTO> dtoList = versionMapper.findAllOrdered();
        return dtoList;
    }

    public void saveVersion(VersionDTO versionDTO) {
        versionMapper.save(versionDTO);
    }

    public CopyrightDTO getCopyright3() {
        return versionMapper.selectVersionAndSiteInfo3();
    }
}
