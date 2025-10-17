package kr.co.shoply.repository;


import kr.co.shoply.entity.Banner;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    @Query("SELECT b FROM Banner b WHERE b.ban_no = :banNo")
    Banner findByBan_no(@Param("banNo") int banNo);
}


