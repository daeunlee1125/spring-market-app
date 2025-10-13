package kr.co.shoply.mapper;

import kr.co.shoply.dto.SiteInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SiteInfoMapper {
    public SiteInfoDTO selectRecentOne(int s_no);
    public void modifyTitles(SiteInfoDTO siteInfoDTO);
    public void modifyLogo1(SiteInfoDTO siteInfoDTO);
    public void modifyLogo2(SiteInfoDTO siteInfoDTO);
    public void modifyLogo3(SiteInfoDTO siteInfoDTO);
    public void modifyFooters(SiteInfoDTO siteInfoDTO);
    public void modifyCsInfos(SiteInfoDTO siteInfoDTO);
    public void modifyCopy(SiteInfoDTO siteInfoDTO);
}
