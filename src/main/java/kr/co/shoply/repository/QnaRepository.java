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
}