package kr.co.shoply.mapper;

import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.SysCouponDTO;
import kr.co.shoply.dto.UserCouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CouponMapper {
    // SysCoupon / 쿠폰 목록 조회
    List<SysCouponDTO> selectCouponList(PageRequestDTO pageRequestDTO);

    // 쿠폰 총 개수
    int selectCouponTotal(PageRequestDTO pageRequestDTO);

    void updateCouponEnd(String cpCode);


    int insertCoupon(SysCouponDTO couponDTO);

    public List<SysCouponDTO> selectUserCoupon3(String mem_id);
    SysCouponDTO selectSysCoupon3(String cp_code);
    public void updateUsedCoupon3(@Param("cp_code") String cpCode, @Param("mem_id") String memId);

    @Select("SELECT COUNT(*) FROM SYS_COUPON WHERE CP_CODE = #{code}")
    boolean existsByCode(String code);



    @Select("SELECT MEM_NAME FROM MEMBER WHERE MEM_ID = #{memId}")
    String selectMemberNameById(String memId);




    // UserCoupon / 쿠폰 목록 조회

    List<UserCouponDTO> selectUserCouponList(PageRequestDTO pageRequestDTO);
    int countUserCouponList(PageRequestDTO pageRequestDTO);



    // UserCoupon / 쿠폰 Detail(cp_no 눌렀을떄 나오는거)
    UserCouponDTO selectUserCouponDetail(String cpNo);



    void updateUserCouponStatus(@Param("cp_no") String cpNo,
                                @Param("cp_stat") int cpStat);


    void updateSysCouponStatus(@Param("cp_code") String cpCode);


}
