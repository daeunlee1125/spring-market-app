package kr.co.shoply.mapper;

import kr.co.shoply.dto.BannerDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BannerMapper{
    public void saveBanner(BannerDTO bannerDTO);
}
