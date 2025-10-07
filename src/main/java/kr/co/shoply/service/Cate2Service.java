package kr.co.shoply.service;

import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.entity.Cate2;
import kr.co.shoply.mapper.Cate2Mapper;
import kr.co.shoply.repository.Cate1Repository;
import kr.co.shoply.repository.Cate2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Cate2Service {
    private final Cate2Mapper cate2Mapper;
    private final Cate2Repository cate2Repository;
    private final ModelMapper modelMapper;
    private final Cate1Repository cate1Repository;

    public Cate2DTO getCate(int cate2_no) {
        return cate2Mapper.select(cate2_no);
    }

    public void updateCate2(Cate2DTO dto) {
        if (dto.getCate1_no() == 0)
            return;

        if (cate2Repository.existsById(dto.getCate2_no())) {
            Cate2 cate2 = cate2Repository.findById(dto.getCate2_no()).get();

            cate2.setCate2_name(dto.getCate2_name());
            cate2.setCate1_no(dto.getCate1_no());
            cate2Repository.save(cate2);
        }else {
            cate2Repository.save(Cate2.builder()
                            .cate2_name(dto.getCate2_name())
                            .cate1_no(dto.getCate1_no())
                            .build());
        }
    }


    public List<Cate2DTO> getCate2List() {
        List<Cate2> cate2List = cate2Repository.findAll();
        List<Cate2DTO> cate2DTOList = new ArrayList<>();
        for (Cate2 cate2 : cate2List) {
            cate2DTOList.add(modelMapper.map(cate2, Cate2DTO.class));
        }
        return cate2DTOList;
    }

}

