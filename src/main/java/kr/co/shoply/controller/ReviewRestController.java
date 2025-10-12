package kr.co.shoply.controller;

import kr.co.shoply.dto.ReviewDTO;
import kr.co.shoply.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviews(
            @RequestParam("prod_no") String prod_no,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        List<ReviewDTO> reviewList = reviewService.getPageList(prod_no, page);
        return ResponseEntity.ok(reviewList);
    }
}