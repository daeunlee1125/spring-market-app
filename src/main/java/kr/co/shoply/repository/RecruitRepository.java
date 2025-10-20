package kr.co.shoply.repository;

import kr.co.shoply.entity.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RecruitRepository extends JpaRepository<Recruit, Integer> {

    /** 전체 (정렬 없이 페이지네이션만) */
    @Query("select r from Recruit r")
    Page<Recruit> fetchAll(Pageable pageable);

    /** 번호 정확 일치 (목록 UI 재사용 위해 Page로 반환) */
    @Query("select r from Recruit r where r.rec_no = :recNo")
    Page<Recruit> findPageByRec_no(@Param("recNo") Integer recNo, Pageable pageable);

    /** 제목 like (대소문자 무시) */
    @Query("""
           select r from Recruit r
           where lower(r.rec_title) like lower(concat('%', :kw, '%'))
           """)
    Page<Recruit> searchByTitle(@Param("kw") String keyword, Pageable pageable);

    /** 부서 정확 일치 */
    @Query("select r from Recruit r where r.rec_department = :dept")
    Page<Recruit> searchByDepartment(@Param("dept") String department, Pageable pageable);

    /** 경력 정확 일치 (예: '신입', '경력', '무관' 등) */
    @Query("select r from Recruit r where r.rec_experience = :exp")
    Page<Recruit> searchByExperience(@Param("exp") String experience, Pageable pageable);

    /** 채용형태 정확 일치 (예: '정규직', '계약직') */
    @Query("select r from Recruit r where r.rec_job_type = :jobType")
    Page<Recruit> searchByJobType(@Param("jobType") String jobType, Pageable pageable);

    /** 상태 정확 일치 (예: '모집중', '마감', '보류') */
    @Query("select r from Recruit r where r.rec_status = :status")
    Page<Recruit> searchByStatus(@Param("status") String status, Pageable pageable);

    @Query("select r from Recruit r where r.rec_no = :recNo")
    Optional<Recruit> findOneByRecNo(@Param("recNo") Integer recNo);
}

