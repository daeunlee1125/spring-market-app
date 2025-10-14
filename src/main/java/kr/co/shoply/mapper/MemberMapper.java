package kr.co.shoply.mapper;

import kr.co.shoply.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 사용
    public MemberDTO selectADDR3(@Param("mem_id") String mem_id, @Param("prod_no") String prod_no); // view.html에 판매자 주소 출력 용도


    //아이디찾기 - 이메일확인
    public int checkEmail(@Param("mem_name") String mem_name, @Param("mem_email") String mem_email);

    // 아이디찾기 - 회원 정보 조회
    public MemberDTO findMemberByNameAndEmail(@Param("mem_name") String mem_name, @Param("mem_email") String mem_email);

    public MemberDTO selectMember3(String mem_id);

}
