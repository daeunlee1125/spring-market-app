package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
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



}
