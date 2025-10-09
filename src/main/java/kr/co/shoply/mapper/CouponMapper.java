package kr.co.shoply.mapper;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.SysCouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CouponMapper {
    // 쿠폰 목록 조회
    List<SysCouponDTO> selectCouponList(PageRequestDTO pageRequestDTO);

    // 쿠폰 총 개수
    int selectCouponTotal(PageRequestDTO pageRequestDTO);

    void updateCouponEnd(String cpCode);


    int insertCoupon(SysCouponDTO couponDTO);



    @Select("SELECT COUNT(*) FROM SYS_COUPON WHERE CP_CODE = #{code}")
    boolean existsByCode(String code);



    @Select("SELECT MEM_NAME FROM MEMBER WHERE MEM_ID = #{memId}")
    String selectMemberNameById(String memId);


}
