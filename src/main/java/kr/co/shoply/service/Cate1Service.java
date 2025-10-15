package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.mapper.Cate1Mapper;
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
    private final Cate1Mapper cate1Mapper;

    public List<Cate1DTO> getCate1Chart(){

        List<Cate1DTO> rawList = cate1Mapper.chartCate1();

        // 매출 기준으로 내림차순 정렬
        rawList.sort((a, b) -> Double.compare(b.getCate1_sell(), a.getCate1_sell()));

        if (rawList.size() <= 3) {
            return rawList; // 3개 이하면 그대로 반환
        }

        // 상위 3개만 남기고, 나머지는 기타로 합산
        List<Cate1DTO> top3 = new ArrayList<>(rawList.subList(0, 3));
        int etcSum = rawList.subList(3, rawList.size())
                .stream()
                .mapToInt(Cate1DTO::getCate1_sell)
                .sum();

        Cate1DTO etc = new Cate1DTO();
        etc.setCate1_name("기타");
        etc.setCate1_sell(etcSum);

        top3.add(etc);
        return top3;


    }


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

    @Transactional
    public List<Cate1DTO> syncCate1(List<Cate1DTO> dtoList) {
        List<Cate1DTO> result = new ArrayList<>();

        // 현재 DB에 존재하는 cate1_no 목록 조회
        List<Integer> existingIds = cate1Repository.findAll()
                .stream()
                .map(Cate1::getCate1_no)
                .toList();

        // 요청으로 들어온 cate1_no 목록
        List<Integer> incomingIds = dtoList.stream()
                .filter(dto -> dto.getCate1_no() != 0)
                .map(Cate1DTO::getCate1_no)
                .toList();


        for (Cate1DTO dto : dtoList) {
            Cate1 saved;
            if (dto.getCate1_no() != 0 && cate1Repository.existsById(dto.getCate1_no())) {
                Cate1 cate1 = cate1Repository.findById(dto.getCate1_no()).get();
                cate1.setCate1_name(dto.getCate1_name());
                saved = cate1;
            } else {
                // 새 항목 추가
                saved = cate1Repository.save(Cate1.builder()
                                .cate1_no(dto.getCate1_no())
                        .cate1_name(dto.getCate1_name())
                        .build());

            }
            dto.setCate1_no(saved.getCate1_no());
            result.add(dto);
        }

        //  삭제 대상: 기존엔 있었는데, 이번 dtoList에는 없는 id
        List<Integer> toDelete = existingIds.stream()
                .filter(id -> !incomingIds.contains(id))
                .toList();

        for (Integer id : toDelete) {
            cate1Repository.deleteById(id);

        }

        return result;
    }

}
