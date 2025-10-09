package kr.co.shoply.repository;


import kr.co.shoply.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT i FROM OrderItem i WHERE i.ord_no = :ord_no")
    List<OrderItem> findByOrd_no(@Param("ord_no") String ord_no);
}
