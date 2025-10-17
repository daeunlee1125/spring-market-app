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

    // 회원 쿠폰 전체 조회 - 순서 재정렬 (cp_exp_date를 마지막에)
    @Query(value = "SELECT uc.cp_no, " +                                    // 0
            "uc.cp_code, " +                                         // 1
            "uc.mem_id, " +                                          // 2
            "TO_DATE(uc.cp_issued_date,'YYYY-MM-DD') as cp_issued_date, " +  // 3
            "TO_DATE(uc.cp_used_date,'YYYY-MM-DD') as cp_used_date, " +      // 4
            "uc.cp_stat, " +                                         // 5
            "sc.cp_type, " +                                         // 6
            "sc.cp_value, " +                                        // 7
            "sc.cp_name, " +                                         // 8
            "sc.cp_min_price, " +                                    // 9
            "sc.cp_exp_date " +                                      // 10
            "FROM USER_COUPON uc " +
            "JOIN SYS_COUPON sc ON uc.cp_code = sc.cp_code " +
            "WHERE uc.mem_id = :mem_id", nativeQuery = true)
    List<Object[]> findUserCouponsNative(@Param("mem_id") String mem_id);
}