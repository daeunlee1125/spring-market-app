package kr.co.shoply.service;

import kr.co.shoply.dto.MemSellerDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.mapper.MemSellerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemSellerService {
    private final MemSellerMapper memSellerMapper;

    public List<MemSellerDTO> getMemSellers2(){
        return memSellerMapper.getSellers2();
    }

    public PageResponseDTO<MemSellerDTO> getMemSellers2Page(PageRequestDTO pageRequestDTO){
        return null;
    }

}
