package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.TermsDTO;
import kr.co.shoply.entity.Member;
import kr.co.shoply.entity.Terms;
import kr.co.shoply.mapper.MemSellerMapper;
import kr.co.shoply.mapper.MemberMapper;
import kr.co.shoply.repository.MemberRepository;
import kr.co.shoply.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemSellerMapper memSellerMapper;
    private final MemberRepository memberRepository;
    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public void insertMember(MemberDTO memberDTO){
        memberDTO.setMem_pass(passwordEncoder.encode(memberDTO.getMem_pass()));

        memberDTO.setMem_stat("정상");

        Member member = modelMapper.map(memberDTO,Member.class);

        memberRepository.save(member);
    }

    @Transactional
    public void insertSeller(MemSellerDTO memSellerDTO) {
        memSellerDTO.setMem_pass(passwordEncoder.encode(memSellerDTO.getMem_pass()));
        memSellerDTO.setMem_stat("운영중");
        memSellerMapper.insertMember(memSellerDTO);
        memSellerMapper.insertMemSeller(memSellerDTO);
    }

    public TermsDTO getTerms(){
        //t_no = count(*) == findById(count(*)) 최신약관 나오도록 수정 예정
        Optional<Terms> optTerms = termsRepository.findById(4);
        if(optTerms.isPresent()){

            Terms terms = optTerms.get();
            return modelMapper.map(terms, TermsDTO.class);
        }

        return null;
    }

    public MemberDTO getMemberAddr(String memId, String prodNo) {
        return memberMapper.selectADDR3(memId, prodNo);
    }
    public MemberDTO getMember(String memId) { return memberMapper.selectMember3(memId); }

    //아이디 중복 체크
    public boolean checkIdExists(String mem_id) {
        int count = memberMapper.countById(mem_id);
        return count > 0;
    }

    //이름과 이메일로 회원 정보 조회 (아이디 찾기용)
    public MemberDTO findMemberByNameAndEmail(String mem_name, String mem_email) {
        log.info("회원 정보 조회 - 이름: {}, 이메일: {}", mem_name, mem_email);
        return memberMapper.findMemberByNameAndEmail(mem_name, mem_email);
    }

    //비밀번호 변경
    public boolean modifyPassword(MemberDTO memberDTO) {

        memberDTO.setMem_pass(passwordEncoder.encode(memberDTO.getMem_pass()));
        int result =  memberMapper.updatePassword(memberDTO);

        return result > 0;
    }

}
