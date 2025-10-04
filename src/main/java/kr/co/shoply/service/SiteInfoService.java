package kr.co.shoply.service;

import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.dto.VersionDTO;
import kr.co.shoply.mapper.SiteInfoMapper;
import kr.co.shoply.mapper.VersionMapper;
import kr.co.shoply.repository.SiteInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteInfoService {

    private final VersionMapper versionMapper;
    private final SiteInfoMapper siteInfoMapper;

    public VersionDTO getVersion() {
        VersionDTO versionDTO = versionMapper.selectRecentOne();
        return versionDTO;
    }

    public SiteInfoDTO getSiteInfo(int s_no) {
        SiteInfoDTO siteInfoDTO = siteInfoMapper.selectRecentOne(s_no);

        return siteInfoDTO;

    }

    public void modifySiteInfo(SiteInfoDTO siteInfoDTO, int type){
        siteInfoDTO.setS_no(1);

        if (type==1){
            siteInfoMapper.modifyTitles(siteInfoDTO);
        }
        else if (type==2){
            siteInfoMapper.modifyLogos(siteInfoDTO);
        }
        else if (type==3){
            siteInfoMapper.modifyFooters(siteInfoDTO);
        }
        else if (type==4){
            siteInfoMapper.modifyCsInfos(siteInfoDTO);
        }
        else if (type==5){
            siteInfoMapper.modifyCopy(siteInfoDTO);
        }
    }


}
