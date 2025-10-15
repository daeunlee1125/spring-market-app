package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;

import kr.co.shoply.dto.PointDTO;
import kr.co.shoply.mapper.AdminMemberMapper;
import kr.co.shoply.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import kr.co.shoply.mapper.AdminMemberMapper;
import kr.co.shoply.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;
    private final MemberRepository memberRepository;

    public PageResponseDTO<MemberDTO> selectMembers(PageRequestDTO pageRequestDTO) {
        List<MemberDTO> list = adminMemberMapper.selectMemberList(pageRequestDTO);
        int total = adminMemberMapper.selectMemberTotal(pageRequestDTO);

        return PageResponseDTO.<MemberDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    public MemberDTO getMemberDetail(String memId) {
        return adminMemberMapper.selectMemberById(memId);
    }


    public void AdminupdateMember(MemberDTO memberDTO) {
        adminMemberMapper.AdminupdateMember(memberDTO);
    }

    public void updateAdminMemberStatus(String memId, String memStat) {
        adminMemberMapper.updateAdminMemberStatus(memId, memStat);
    }


    @Transactional
    public void AdminupdateRank(List<MemberDTO> members) {
        for (MemberDTO member : members) {
            adminMemberMapper.AdminupdateRank(member.getMem_id(), member.getMem_rank());
        }
    }

    // 회원 포인트

    public PageResponseDTO<PointDTO> selectAdminPointList(PageRequestDTO pageRequestDTO) {
        List<PointDTO> list = adminMemberMapper.selectAdminPointList(pageRequestDTO);
        int total = adminMemberMapper.selectAdminTotalCount(pageRequestDTO);
        return PageResponseDTO.<PointDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }


}


}

