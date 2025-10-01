package kr.co.shoply.repository;

import kr.co.shoply.entity.MemSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemSellerRepository extends JpaRepository<MemSeller, String> {
}
