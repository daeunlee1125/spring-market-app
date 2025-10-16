package kr.co.shoply.service;

import kr.co.shoply.dto.BannerDTO;
import kr.co.shoply.entity.Banner;
import kr.co.shoply.mapper.BannerMapper;
import kr.co.shoply.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;
    private final BannerMapper bannerMapper;

    public void saveBanner(BannerDTO bannerDTO){
        bannerMapper.saveBanner(bannerDTO);
    }

    public List<BannerDTO> mainBanners2(){
        return bannerMapper.getBanners2();
    }

    public List<BannerDTO> getBanners2(){
        List<Banner> banners = bannerRepository.findAll();
        List<BannerDTO> bannerDTOS = new ArrayList<>();

        for (Banner banner:banners){
            bannerDTOS.add(modelMapper.map(banner,BannerDTO.class));
        }

        return bannerDTOS;
    }

    public void deleteAllById(List<Integer> ids){
        for (Integer id:ids){
            bannerRepository.deleteById(id);
        }
    }

    public void changeBannerStatus(int ban_no, int ban_status) {
        if (ban_status==0){
            bannerMapper.statusToZero(ban_no);
        }else if (ban_status==1){
            bannerMapper.statusToOne(ban_no);
        }
    }

    public String getProductBanner3(){
        return bannerMapper.selectProductBanner3();
    }
}
