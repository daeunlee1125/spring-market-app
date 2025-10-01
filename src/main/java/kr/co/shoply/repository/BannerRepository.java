package kr.co.shoply.repository;


import kr.co.shoply.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Integer> {


}
