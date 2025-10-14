package kr.co.shoply.mapper;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.AdInfoDTO;
import kr.co.shoply.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 사용
    MemberDTO selectADDR3(@Param("mem_id") String mem_id, @Param("prod_no") String prod_no); // view.html에 판매자 주소 출력 용도

    //아이디 중복 체크
    int countById(String mem_id);

    //회원가입 - 이메일확인
    int checkRegEmail(@Param("mem_email") String mem_email);

    //아이디 찾기 - 이메일확인
    int checkEmail(@Param("mem_name") String mem_name, @Param("mem_email") String mem_email);

    // 아이디 찾기 - 회원 정보 조회
    MemberDTO findMemberByNameAndEmail(@Param("mem_name") String mem_name, @Param("mem_email") String mem_email);

    //비밀번호 찾기 - 이메일확인
    int checkEmailById(@Param("mem_id") String mem_id, @Param("mem_email") String mem_email);

    //비밀번호 변경
    int updatePassword(MemberDTO memberDTO);

    public MemberDTO selectMember3(String mem_id);

    public AdInfoDTO regCnt2();

}
