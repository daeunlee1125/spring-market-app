package kr.co.shoply.repository;

import kr.co.shoply.entity.Cate2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cate2Repository extends JpaRepository<Cate2,Integer> {

    //1차 카테고리로 2차 카테고리 조회
    @Query("SELECT c FROM Cate2 c WHERE c.cate1_no = :cate1_no")
    public List<Cate2> findAllByCate1No(int cate1_no);
}
