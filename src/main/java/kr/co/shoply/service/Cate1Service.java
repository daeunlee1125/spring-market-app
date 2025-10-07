package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.repository.Cate1Repository;
import kr.co.shoply.repository.Cate2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Cate1Service {
    private final Cate1Repository cate1Repository;
    private final ModelMapper modelMapper;

    public void updateCate1(Cate1DTO dto) {

        if (cate1Repository.existsById(dto.getCate1_no())) {
            Cate1 cate1 = cate1Repository.findById(dto.getCate1_no()).get();

            cate1.setCate1_name(dto.getCate1_name());
            cate1Repository.save(cate1);
        } else {
            cate1Repository.save(Cate1.builder()
                    .cate1_name(dto.getCate1_name())
                    .build());
        }

        cate1Repository.flush();



    }

    public List<Cate1DTO> getCate1List()
    {
        List<Cate1> cate1List = cate1Repository.findAll();
        List<Cate1DTO> cate1DTOList = new ArrayList<>();
        for (Cate1 cate1 : cate1List) {
            cate1DTOList.add(modelMapper.map(cate1, Cate1DTO.class));
        }

        return cate1DTOList;
    }
}
