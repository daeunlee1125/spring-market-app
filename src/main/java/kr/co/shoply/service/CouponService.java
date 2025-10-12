package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.dto.SysCouponDTO;
import kr.co.shoply.dto.UserCouponDTO;
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
        // SYS_COUPON : 1 → 0
        couponMapper.updateCouponEnd(cpCode);

        // USER_COUPON : 1 → 3 (발급 종료 반영)
        couponMapper.updateSysCouponStatus(cpCode);
    }


    // 랜덤 쿠폰 11자리 생성
    private String generateCouponCode() {
        Random random = new Random();
        String code;

        while (true) {
            // 11자리 랜덤 숫자 생성
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 11; i++) {
                sb.append(random.nextInt(10));
            }
            code = sb.toString();

            //DB 중복 검사
            boolean exists = couponMapper.existsByCode(code);
            if (!exists) break; // 중복이 없으면 사용

            //중복이면 다시 루프
        }
        return code;
    }


    public String findMemberNameById(String memId) {
        return couponMapper.selectMemberNameById(memId);
    }



    public PageResponseDTO<UserCouponDTO> selectUserCouponList(PageRequestDTO pageRequestDTO) {
        List<UserCouponDTO> list = couponMapper.selectUserCouponList(pageRequestDTO);
        int total = couponMapper.countUserCouponList(pageRequestDTO);

        return PageResponseDTO.<UserCouponDTO>builder()
                .dtoList(list)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


    public UserCouponDTO selectUserCouponDetail(String cpNo) {
        return couponMapper.selectUserCouponDetail(cpNo);
    }

    public void updateUserCouponStatus(String cpNo, int cpStat) {
        couponMapper.updateUserCouponStatus(cpNo, cpStat);
    }








}
