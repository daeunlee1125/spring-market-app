package kr.co.shoply.repository;


import kr.co.shoply.entity.CsFaq;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CsFaqRepository extends JpaRepository<CsFaq, Integer> {

    // 목록 조회 (정렬은 Service에서 Sort로 전달)
    List<CsFaq> findByCsFaqCate1AndCsFaqCate2(String csFaqCate1, String csFaqCate2, Sort sort);
    List<CsFaq> findByCsFaqCate1(String csFaqCate1, Sort sort);
    List<CsFaq> findAll(Sort sort);

    // 1차 카테고리 목록
    @Query("select distinct f.csFaqCate1 from CsFaq f order by f.csFaqCate1 asc")
    List<String> findAllCat1();

    // 선택된 1차 기준 2차 카테고리 목록
    @Query("select distinct f.csFaqCate2 from CsFaq f where f.csFaqCate1 = :cat1 order by f.csFaqCate2 asc")
    List<String> findCat2ByCat1(@Param("cat1") String cat1);

}
