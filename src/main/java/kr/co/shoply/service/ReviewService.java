package kr.co.shoply.service;

import kr.co.shoply.dto.ReviewDTO;
import kr.co.shoply.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public double selectByProdNo(String prod_no) {
        return reviewMapper.select(prod_no);
    }
}
