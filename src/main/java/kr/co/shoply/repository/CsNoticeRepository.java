package kr.co.practice.repository;

import kr.co.practice.entity.CsNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsNoticeRepository extends JpaRepository<CsNotice, Integer> {
}
