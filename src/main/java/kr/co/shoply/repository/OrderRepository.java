package kr.co.shoply.repository;

import kr.co.shoply.entity.Order;
import kr.co.shoply.entity.Qna;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT COUNT(o) FROM Order o WHERE o.mem_id = :mem_id")
    long countByMem_id(@Param("mem_id") String mem_id);

    @Query("SELECT o FROM Order o WHERE o.mem_id = :mem_id ORDER BY o.ord_date DESC")
    List<Order> findTop5ByMem_idOrderByOrd_dateDesc(@Param("mem_id") String mem_id);

    // ⭐ @Query 어노테이션을 사용하여 명시적인 쿼리 작성
    @Query("SELECT o FROM Order o WHERE o.mem_id = :memId ORDER BY o.ord_date DESC")
    Page<Order> findByMem_id(@Param("memId") String memId, Pageable pageable);
}