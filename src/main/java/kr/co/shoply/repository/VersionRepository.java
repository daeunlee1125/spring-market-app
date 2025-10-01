package kr.co.sboard.repository;

import kr.co.practice.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Integer> {
}
