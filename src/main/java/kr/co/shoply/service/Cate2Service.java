package kr.co.shoply.service;

import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.mapper.Cate2Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Cate2Service {
    private final Cate2Mapper cate2Mapper;

    public Cate2DTO getCate(int cate2_no) {
        return cate2Mapper.select(cate2_no);
    }

}
