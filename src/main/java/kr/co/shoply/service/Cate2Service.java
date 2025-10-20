package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.entity.Cate2;
import kr.co.shoply.mapper.Cate2Mapper;
import kr.co.shoply.repository.Cate1Repository;
import kr.co.shoply.repository.Cate2Repository;
import kr.co.shoply.repository.ProductRepository;
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
    private final ProductRepository productRepository;

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

    @Transactional
    public void syncCate2(List<Cate2DTO> dtoList) {
        //  현재 DB에 존재하는 cate2_no 목록 조회
        List<Integer> existingIds = cate2Repository.findAll()
                .stream()
                .map(Cate2::getCate2_no)
                .toList();

        //  이번 요청에서 유지되어야 할 cate2_no 목록
        List<Integer> incomingIds = dtoList.stream()
                .filter(dto -> dto.getCate2_no() != 0)
                .map(Cate2DTO::getCate2_no)
                .toList();

        //  update / insert
        for (Cate2DTO dto : dtoList) {
            if (dto.getCate1_no() == 0) continue;

            if (dto.getCate2_no() != 0 && cate2Repository.existsById(dto.getCate2_no())) {
                Cate2 cate2 = cate2Repository.findById(dto.getCate2_no()).get();

                cate2.setCate2_name(dto.getCate2_name());
                cate2.setCate1_no(dto.getCate1_no());
            } else {
                cate2Repository.save(Cate2.builder()
                                .cate2_no(dto.getCate2_no())
                        .cate2_name(dto.getCate2_name())
                        .cate1_no(dto.getCate1_no())
                        .build());
            }
        }

        //  삭제 대상: 기존엔 있었지만, 이번 dtoList에는 없는 id
        List<Integer> toDelete = existingIds.stream()
                .filter(id -> !incomingIds.contains(id))
                .toList();


        for (Integer id : toDelete) {
                cate2Repository.deleteById(id);

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

