package kr.co.shoply.security;

import kr.co.shoply.entity.Member;
import kr.co.shoply.entity.MemberSocial;
import kr.co.shoply.repository.MemberRepository;
import kr.co.shoply.repository.MemberSocialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 구글에서 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 어떤 소셜 로그인인지 확인 (google, naver, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. 구글에서 받은 사용자 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialId = attributes.get("sub").toString();  // 구글 고유 ID
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();

        log.info("소셜 로그인 - Type: {}, ID: {}, Email: {}", registrationId, socialId, email);

        // 4. DB에서 소셜 로그인 정보 찾기
        Optional<MemberSocial> optionalSocial = memberSocialRepository
                .findBySocialTypeAndSocialId(registrationId, socialId);

        String memId;

        if (optionalSocial.isEmpty()) {
            // 5. 처음 로그인 - 회원가입 처리
            memId = registrationId + "_" + socialId.substring(0, Math.min(10, socialId.length()));

            // MEMBER 테이블에 저장
            Member member = Member.builder()
                    .mem_id(memId)
                    .mem_email(email)
                    .mem_name(name)
                    .mem_level(1)  // int 타입
                    .mem_stat("정상")
                    .mem_pass("SOCIAL_LOGIN_NO_PASSWORD")  // 소셜 로그인은 비밀번호 없음 더미 비밀번호
                    .build();
            memberRepository.save(member);

            // MEMBER_SOCIAL 테이블에 저장
            MemberSocial memberSocial = MemberSocial.builder()
                    .memId(memId)
                    .socialType(registrationId)
                    .socialId(socialId)
                    .socialEmail(email)
                    .build();
            memberSocialRepository.save(memberSocial);

            log.info("새 소셜 회원 가입 완료 - memId: {}", memId);

        } else {
            // 6. 이미 가입된 회원
            memId = optionalSocial.get().getMemId();
            log.info("기존 소셜 회원 로그인 - memId: {}", memId);
        }

        // 7. 기존 UserDetails 로드
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(memId);

        // 8. MyUserDetails로 캐스팅하고 attributes 설정
        MyUserDetails myUserDetails = (MyUserDetails) userDetails;
        myUserDetails.setAttributes(attributes);  // OAuth2 정보 추가

        return myUserDetails;
    }
}