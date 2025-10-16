package kr.co.shoply.repository;

import kr.co.shoply.entity.CsNotice;
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

//    @Query("select distinct n.cs_type from CsNotice n order by n.cs_type asc")
//    List<String> findAllTypes();
}
