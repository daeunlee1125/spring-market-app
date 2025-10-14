package kr.co.shoply.repository;

import kr.co.shoply.dto.UserCouponDTO;
import kr.co.shoply.entity.UserCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, String> {

    @Query("SELECT COUNT(c) FROM UserCoupon c WHERE c.mem_id = :mem_id")
    long countByMem_id(@Param("mem_id") String mem_id);

    // 회원 쿠폰 전체 조회
    @Query(value = "SELECT uc.cp_no, uc.cp_code, uc.mem_id, " +
            "TO_DATE(uc.cp_issued_date,'YYYY-MM-DD') as cp_issued_date, " +
            "TO_DATE(uc.cp_used_date,'YYYY-MM-DD') as cp_used_date, " +
            "uc.cp_stat, " +
            "sc.cp_type, sc.cp_value, sc.cp_name, sc.cp_min_price, sc.cp_exp_date " +  // cp_min_price 추가!
            "FROM USER_COUPON uc JOIN SYS_COUPON sc ON uc.cp_code = sc.cp_code " +
            "WHERE uc.mem_id = :mem_id", nativeQuery = true)
    List<Object[]> findUserCouponsNative(@Param("mem_id") String mem_id);
}
