package kr.co.shoply.repository;

import kr.co.shoply.entity.Review;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 최근 5개 리뷰
    @Query("SELECT r FROM Review r WHERE r.mem_id = :mem_id ORDER BY r.rev_rdate DESC")
    List<Review> findTop5ByMem_idOrderByRev_rdateDesc(@Param("mem_id") String mem_id);

    // 전체 리뷰 조회
    @Query("SELECT r FROM Review r WHERE r.mem_id = :mem_id ORDER BY r.rev_rdate DESC")
    List<Review> findByMem_idOrderByRev_rdateDesc(@Param("mem_id") String mem_id);

    // 페이지네이션
    @Query("SELECT r FROM Review r WHERE r.mem_id = :mem_id ORDER BY r.rev_rdate DESC")
    Page<Review> findByMem_id(@Param("mem_id") String mem_id, Pageable pageable);

    // ✅ 수정: 최신 리뷰만 조회 (중복 리뷰 대비)
    @Query(value = "SELECT r FROM Review r WHERE r.mem_id = :mem_id AND r.prod_no = :prod_no ORDER BY r.rev_rdate DESC LIMIT 1")
    Optional<Review> findByMem_idAndProd_no(@Param("mem_id") String mem_id, @Param("prod_no") String prod_no);

    // 또는 네이티브 쿼리 사용 (더 안정적)
    @Query(value = "SELECT * FROM Review WHERE mem_id = :mem_id AND prod_no = :prod_no ORDER BY rev_rdate DESC LIMIT 1",
            nativeQuery = true)
    Optional<Review> findLatestReviewByMemIdAndProdNo(@Param("mem_id") String mem_id, @Param("prod_no") String prod_no);
}
