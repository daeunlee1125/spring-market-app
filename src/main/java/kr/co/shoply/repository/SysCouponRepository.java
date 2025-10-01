package kr.co.shoply.repository;

import kr.co.sboard.entity.market.SysCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysCouponRepository extends JpaRepository<SysCoupon, String> {
}