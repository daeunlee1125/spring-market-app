package kr.co.shoply.security;


import kr.co.shoply.entity.Member;
import kr.co.shoply.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mem_id) throws UsernameNotFoundException {
        
        // 사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        Optional<Member> optUser = userRepository.findById(mem_id);
        if (optUser.isPresent()) {
            Member member = optUser.get();

            if (member.getMem_stat().equals("휴면") || member.getMem_stat().equals("탈퇴")) {
                return null;
            } else {
                // 인증 객체 생성
                MyUserDetails userDetails = MyUserDetails.builder()
                        .member(member)
                        .build();

                // 반환되는 인증객체가 Security Context Holder에 Authentication으로 저장
                return userDetails;
            }
        }
        // 아이디가 없는 경우
        return null;
    }

}
