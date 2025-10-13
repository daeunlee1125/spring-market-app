package kr.co.shoply.repository;

import kr.co.shoply.entity.UserCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, String> {
    @Query("SELECT COUNT(c) FROM UserCoupon c WHERE c.mem_id = :mem_id")
    long countByMem_id(@Param("mem_id") String mem_id);
}