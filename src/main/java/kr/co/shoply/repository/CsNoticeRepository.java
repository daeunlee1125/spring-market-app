package kr.co.shoply.repository;

import kr.co.shoply.entity.CsNotice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CsNoticeRepository extends JpaRepository<CsNotice, Integer> {
    // 카테고리(예: 고객서비스/안전거래/...) 로 필터, 정렬 인자 받기
    List<CsNotice> findByCsType(String csType, Sort sort);

}
