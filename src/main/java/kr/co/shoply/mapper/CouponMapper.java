package kr.co.shoply.mapper;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.SysCouponDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {
    // 쿠폰 목록 조회
    List<SysCouponDTO> selectCouponList(PageRequestDTO pageRequestDTO);

    // 쿠폰 총 개수
    int selectCouponTotal(PageRequestDTO pageRequestDTO);

    void updateCouponEnd(String cpCode);


    int insertCoupon(SysCouponDTO couponDTO);




}
