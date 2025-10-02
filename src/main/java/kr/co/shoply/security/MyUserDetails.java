package kr.co.shoply.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Builder
public class MyUserDetails implements UserDetails {

    private kr.co.shoply.entity.Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 계정 목록 리스트 생성, 인가 처리에 사용
        List<GrantedAuthority> authorities = new ArrayList<>();
        /**** 중요!! 계정 권한 앞에 접두어 ROLE_ 작성! ****/
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getMem_level()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getMem_pass();
    }

    @Override
    public String getUsername() {
        /* security에서 username는 ID다!! */
        return member.getMem_id();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 (False : 만료, True : 살아 있음)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 잠겨 있으면(false) 로그인을 못하겠죵
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부 (3개월마다 만료되든지...그런 거)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부 (false면 로그인을 못하겟죵)
        return true;
    }
}
