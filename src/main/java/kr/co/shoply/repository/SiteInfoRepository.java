package kr.co.shoply.repository;

import kr.co.shoply.entity.SiteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteInfoRepository extends JpaRepository<SiteInfo, String> {

}
