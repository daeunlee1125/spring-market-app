package kr.co.shoply.repository;



import kr.co.shoply.entity.Qna;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

    @Query("""
           select distinct q.qCate1
             from Qna q
            where q.qCate1 is not null and q.qCate1 <> ''
            order by q.qCate1 asc
           """)
    List<String> findAllCat1();

    @Query("""
           select distinct q.qCate2
             from Qna q
            where q.qCate1 = :cat1
              and q.qCate2 is not null and q.qCate2 <> ''
            order by q.qCate2 asc
           """)
    List<String> findCat2ByCat1(@Param("cat1") String cat1);

    @Query("""
           select q
             from Qna q
            where (:cat1 is null or q.qCate1 = :cat1)
              and (:cat2 is null or q.qCate2 = :cat2)
           """)
    List<Qna> findList(@Param("cat1") String cat1,
                       @Param("cat2") String cat2,
                       Sort sort);
}