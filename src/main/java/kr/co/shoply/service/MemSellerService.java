package kr.co.shoply.service;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.MemberDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.entity.MemSeller;
import kr.co.shoply.entity.Member;
import kr.co.shoply.mapper.MemSellerMapper;
import kr.co.shoply.repository.MemSellerRepository;
import kr.co.shoply.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemSellerService {
    private final MemSellerMapper memSellerMapper;
    private final MemSellerRepository memSellerRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper  modelMapper;
    private final PasswordEncoder  passwordEncoder;

    public List<MemSellerDTO> getMemSellers2(){
        return memSellerMapper.getSellers2();
    }

    public PageResponseDTO<MemSellerDTO> getMemSellers2Page(PageRequestDTO pageRequestDTO){

        List<MemSellerDTO> memList = memSellerMapper.getSellerPage2(pageRequestDTO);
        int total = memSellerMapper.selectCountTotal2(pageRequestDTO);

        return PageResponseDTO.<MemSellerDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(memList)
                .total(total)
                .build();
    }

    public void modifyStat(MemberDTO memberDTO){
        memSellerMapper.updateStat2(memberDTO);
    }
    public void deleteSeller(String mem_id){
        memSellerRepository.deleteById(mem_id);
        memberRepository.deleteById(mem_id);
    }

    public void registerSeller(MemSellerDTO memSellerDTO){
        memSellerDTO.setMem_pass(passwordEncoder.encode(memSellerDTO.getMem_pass()));
        memSellerDTO.setMem_stat("운영중");
        memSellerDTO.setMem_level(2);
        memberRepository.save(modelMapper.map( MemberDTO.builder()
                        .mem_id(memSellerDTO.getMem_id())
                        .mem_pass(memSellerDTO.getMem_pass())
                        .mem_name(memSellerDTO.getMem_name())
                        .mem_hp(memSellerDTO.getMem_hp())
                        .mem_zip(memSellerDTO.getMem_zip())
                        .mem_addr1(memSellerDTO.getMem_addr1())
                        .mem_addr2(memSellerDTO.getMem_addr2())
                        .mem_stat(memSellerDTO.getMem_stat())
                        .mem_level(memSellerDTO.getMem_level())
                .build(), Member.class));
        memSellerRepository.save(modelMapper.map(memSellerDTO, MemSeller.class));

    }

    public PageResponseDTO<MemSellerDTO> getSellerSales(PageRequestDTO pageRequestDTO){
        List<MemSellerDTO> sellersDTO = memSellerMapper.sellerWithSales(pageRequestDTO);
        int total = memSellerMapper.selectCountTotal2(pageRequestDTO);

        return PageResponseDTO.<MemSellerDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(sellersDTO)
                .total(total)
                .build();
    }

    public PageResponseDTO<MemSellerDTO> getSellerRange(PageRequestDTO pageRequestDTO, String range){
        List<MemSellerDTO> sellersDTO = memSellerMapper.sellerWithRange(pageRequestDTO, range);
        int total = memSellerMapper.selectCountTotal2(pageRequestDTO);

        return PageResponseDTO.<MemSellerDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(sellersDTO)
                .total(total)
                .build();
    }

}
