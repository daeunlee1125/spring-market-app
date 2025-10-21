package kr.co.shoply.repository;

import kr.co.shoply.entity.MemberSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberSocialRepository extends JpaRepository<MemberSocial, Long> {

    // 소셜 타입과 소셜 ID로 찾기
    Optional<MemberSocial> findBySocialTypeAndSocialId(String socialType, String socialId);

    // 회원 ID로 찾기
    Optional<MemberSocial> findByMemId(String memId);
}