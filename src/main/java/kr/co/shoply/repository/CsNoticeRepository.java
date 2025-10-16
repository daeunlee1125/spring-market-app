package kr.co.shoply.repository;

import kr.co.shoply.entity.CsNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CsNoticeRepository extends JpaRepository<CsNotice, Integer> {

    @Query("""
           select n
           from CsNotice n
           where (:type is null or n.cs_type = :type)
           order by n.cs_rdate desc, n.cs_no desc
           """)
    List<CsNotice> findList(@Param("type") String type);

    @Query(
            value = """
                select n
                  from CsNotice n
                 where (:type is null or n.cs_type = :type)
                 order by n.cs_rdate desc, n.cs_no desc
                """,
            countQuery = """
                select count(n)
                  from CsNotice n
                 where (:type is null or n.cs_type = :type)
                """
    )
    Page<CsNotice> findPage(@Param("type") String type, Pageable pageable);

    @Query("""
    select n
    from CsNotice n
    order by n.cs_rdate desc
""")
    List<CsNotice> findAllOrdered(Pageable pageable);

    default List<CsNotice> findTopN(int n) {
        return findAllOrdered(PageRequest.of(0, n));
    }
}
