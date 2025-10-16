package kr.co.shoply.repository;


import jakarta.transaction.Transactional;
import kr.co.shoply.entity.CsFaq;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CsFaqRepository extends JpaRepository<CsFaq, Integer> {


    //  1차 카테고리로 검색
    @Query("SELECT f FROM CsFaq f WHERE f.cs_faq_cate1 = :cate1")
    List<CsFaq> findByCate1(@Param("cate1") String cate1);

    // 1차 + 2차 카테고리로 검색
    @Query("SELECT f FROM CsFaq f WHERE f.cs_faq_cate1 = :cate1 AND f.cs_faq_cate2 = :cate2")
    List<CsFaq> findByCate1AndCate2(@Param("cate1") String cate1, @Param("cate2") String cate2);

    // 조회수 증가 쿼리
    @Modifying
    @Transactional
    @Query("UPDATE CsFaq f SET f.cs_faq_hit = f.cs_faq_hit + 1 WHERE f.cs_faq_no = :cs_faq_no")
    void increaseHit(@Param("cs_faq_no") int cs_faq_no);

    // 1차 카테고리 전체 목록 (사이드바)
    @Query("SELECT DISTINCT f.cs_faq_cate1 FROM CsFaq f ORDER BY f.cs_faq_cate1 ASC")
    List<String> findAllCat1();

    // 선택된 1차 기준 2차 카테고리 목록 (사이드바)
    @Query("""
       SELECT DISTINCT f.cs_faq_cate2
       FROM CsFaq f
       WHERE f.cs_faq_cate1 = :cate1
       ORDER BY f.cs_faq_cate2 ASC
       """)
    List<String> findCat2ByCat1(@Param("cate1") String cate1);


}