package kr.co.shoply.repository;

import kr.co.shoply.entity.Review;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 최근 5개 리뷰
    @Query("SELECT r FROM Review r WHERE r.mem_id = :mem_id ORDER BY r.rev_rdate DESC")
    List<Review> findTop5ByMem_idOrderByRev_rdateDesc(@Param("mem_id") String mem_id);

    // 전체 리뷰 조회
    @Query("SELECT r FROM Review r WHERE r.mem_id = :mem_id ORDER BY r.rev_rdate DESC")
    List<Review> findByMem_idOrderByRev_rdateDesc(@Param("mem_id") String mem_id);
}
