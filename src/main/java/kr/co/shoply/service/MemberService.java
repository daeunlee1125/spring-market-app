package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.TermsDTO;
import kr.co.shoply.entity.Member;
import kr.co.shoply.entity.Terms;
import kr.co.shoply.mapper.MemSellerMapper;
import kr.co.shoply.repository.MemberRepository;
import kr.co.shoply.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public void insertMember(MemberDTO memberDTO){

        Member member = modelMapper.map(memberDTO,Member.class);
        memberRepository.save(member);
    }

    @Transactional
    public void insertSeller(MemSellerDTO memSellerDTO) {
        memSellerMapper.insertMember(memSellerDTO);
        memSellerMapper.insertMemSeller(memSellerDTO);
    }

    public TermsDTO getTerms(){
        //t_no = count(*) == findById(count(*)) 최신약관 나오도록 수정 예정
        Optional<Terms> optTerms = termsRepository.findById(1);
        if(optTerms.isPresent()){

            Terms terms = optTerms.get();
            return modelMapper.map(terms, TermsDTO.class);
        }

        return null;
    }


}
