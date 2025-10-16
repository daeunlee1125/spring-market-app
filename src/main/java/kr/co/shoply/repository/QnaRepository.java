package kr.co.shoply.repository;

import kr.co.shoply.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

    @Query("SELECT COUNT(q) FROM Qna q WHERE q.mem_id = :mem_id")
    long countByMem_id(@Param("mem_id") String mem_id);

    @Query("SELECT q FROM Qna q WHERE q.mem_id = :mem_id ORDER BY q.q_rdate DESC")
    List<Qna> findTop5ByMem_idOrderByQ_rdateDesc(@Param("mem_id") String mem_id);

    /** 1차 카테고리 목록 */
    @Query("""
           select distinct q.q_cate1
             from Qna q
            where q.q_cate1 is not null and q.q_cate1 <> ''
            order by q.q_cate1 asc
           """)
    List<String> findAllCat1();

    /** 선택된 1차 기준 2차 카테고리 목록 */
    @Query("""
           select distinct q.q_cate2
             from Qna q
            where q.q_cate1 = :cat1
              and q.q_cate2 is not null and q.q_cate2 <> ''
            order by q.q_cate2 asc
           """)
    List<String> findCat2ByCat1(@Param("cat1") String cat1);

    /** QnA 목록 (필터링, 최신순 정렬 내장) */
    @Query("""
           select q
             from Qna q
            where (:cat1 is null or q.q_cate1 = :cat1)
              and (:cat2 is null or q.q_cate2 = :cat2)
            order by q.q_no desc
           """)
    List<Qna> findList(@Param("cat1") String cat1,
                       @Param("cat2") String cat2);
}