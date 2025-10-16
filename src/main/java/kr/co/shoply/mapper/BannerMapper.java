package kr.co.shoply.mapper;

import kr.co.shoply.dto.BannerDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper{
    public void saveBanner(BannerDTO bannerDTO);
    public void statusToZero(int ban_no);
    public void statusToOne(int ban_no);

    public List<BannerDTO> getBanners2();

}
