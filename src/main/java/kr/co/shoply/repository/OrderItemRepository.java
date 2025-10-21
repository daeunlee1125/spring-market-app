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

    // 판매자 실적 조회 - 누적 판매액, 주문수, 반품수
    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN i.item_stat = 4 THEN (p.prod_price - p.prod_sale) * i.item_cnt ELSE 0 END), 0) AS tot_sell_price, " +
            "COUNT(DISTINCT i.ord_no) AS ord_cnt, " +
            "SUM(CASE WHEN i.item_stat = 6 THEN 1 ELSE 0 END) AS return_cnt " +
            "FROM order_item i " +
            "JOIN product p ON i.prod_no = p.prod_no " +
            "WHERE p.mem_id = :memId",
            nativeQuery = true)
    List<Object[]> findSellerStats(@Param("memId") String memId);
}
