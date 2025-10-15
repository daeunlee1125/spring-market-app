package kr.co.shoply.repository;

import kr.co.shoply.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    // @Query 사용으로 명시적 쿼리 작성
    @Query("SELECT p FROM Point p WHERE p.mem_id = :mem_id ORDER BY p.p_date DESC")
    List<Point> findByMem_idOrderByP_dateDesc(@Param("mem_id") String mem_id);

    @Query("SELECT SUM(p.p_point) FROM Point p WHERE p.mem_id = :mem_id")
    Integer getTotalPointsByMem_id(@Param("mem_id") String mem_id);
}