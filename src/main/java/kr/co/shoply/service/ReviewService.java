package kr.co.shoply.service;

import kr.co.shoply.dto.ReviewDTO;
import kr.co.shoply.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public List<ReviewDTO> getReviews3(String prodNo){
        List<ReviewDTO> reviewDTOS = reviewMapper.selectAll3(prodNo);

        for(ReviewDTO reviewDTO : reviewDTOS){
            if(reviewDTO.getMem_id()!=null){
                String str = reviewDTO.getMem_id().substring(0,3) + "****";
                reviewDTO.setPrivateMemId(str);
            }else{
                reviewDTO.setPrivateMemId("비회원");
            }
            log.info(reviewDTO.getPrivateMemId());
        }

        return reviewDTOS;
    }

    public List<ReviewDTO> getPageList(String prodNo, int page){
        // 1. 변수 이름과 계산식을 올바르게 수정
        int limit = 5; // 한 페이지에 보여줄 개수 (size/limit)
        int offset = (page - 1) * limit; // 건너뛸 개수 (offset)

        // 2. 올바른 변수를 Mapper에 전달
        List<ReviewDTO> reviewDTOS = reviewMapper.selectPageList3(prodNo, offset, limit);

        // 3. 아이디 마스킹 로직 추가
        for(ReviewDTO reviewDTO : reviewDTOS){
            if(reviewDTO.getMem_id()!=null){
                String str = reviewDTO.getMem_id().substring(0,3) + "****";
                reviewDTO.setPrivateMemId(str);
            }else{
                reviewDTO.setPrivateMemId("비회원");
            }
        }
        return reviewDTOS;
    }

    public int getCountReviews(String prodNo){
        return reviewMapper.selectCountAll3(prodNo);
    }
}
