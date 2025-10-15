package kr.co.shoply.service;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.entity.Cate2;
import kr.co.shoply.repository.Cate1Repository;
import kr.co.shoply.repository.Cate2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {
    private final Cate1Repository cate1Repository;
    private final Cate2Repository cate2Repository;
    private final ModelMapper modelMapper;

    public List<Cate1DTO> getAllCate1(){

        List<Cate1> cate1List = cate1Repository.findAll();
        log.info("cate1List={}",cate1List);

        return cate1List.stream()
                .map(cate1 -> modelMapper.map(cate1, Cate1DTO.class))
                .toList();
    }

    public List<Cate2DTO> getCate2ByCate1(String cate1){
        int cate1_no = Integer.parseInt(cate1);

        List<Cate2> cate2List = cate2Repository.findAllByCate1No(cate1_no);

        return cate2List.stream()
                .map(cate2 -> modelMapper.map(cate2, Cate2DTO.class))
                .toList();
    }
}
