package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.SysCouponDTO;
import kr.co.shoply.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper couponMapper;

    public PageResponseDTO<SysCouponDTO> selectCouponList(PageRequestDTO pageRequestDTO) {
        List<SysCouponDTO> list = couponMapper.selectCouponList(pageRequestDTO);
        int total = couponMapper.selectCouponTotal(pageRequestDTO);

        return PageResponseDTO.<SysCouponDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }


    @Transactional
    public void registerCoupon(SysCouponDTO couponDTO) {
        // 쿠폰번호 11자리 숫자 랜덤 생성
        String cpCode = generateCouponCode();
        couponDTO.setCp_code(cpCode);

        couponDTO.setCp_stat(1);

        couponMapper.insertCoupon(couponDTO);
    }


    @Transactional
    public void endCoupon(String cpCode) {
        couponMapper.updateCouponEnd(cpCode);
    }


    // 랜덤 11자리 숫자 쿠폰번호 생성
    private String generateCouponCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }





}
