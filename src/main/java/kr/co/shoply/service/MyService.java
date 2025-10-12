package kr.co.shoply.service;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.*;
import kr.co.shoply.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyService {

    private final ProductRepository productRepository; // 상품명 조회를 위해 추가
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final SysCouponRepository sysCouponRepository;
    private final UserCouponRepository userCouponRepository;
    private final PointRepository pointRepository;
    private final QnaRepository qnaRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    // 마이페이지 홈에 필요한 데이터 조회
    public MyPageHomeDTO getMyPageHomeData(String mem_id) {
        // 주문, 쿠폰, 포인트, 문의 갯수 조회
        long orderCount = orderRepository.countByMem_id(mem_id);
        long couponCount = userCouponRepository.countByMem_id(mem_id);
        long qnaCount = qnaRepository.countByMem_id(mem_id);

        // 포인트 총합 조회 (PointRepository에 추가 메서드 필요)
        // int pointTotal = pointRepository.getTotalPointsByMemId(memId).orElse(0);

        // 최근 주문 내역 조회
        List<Order> orders = orderRepository.findTop5ByMem_idOrderByOrd_dateDesc(mem_id);
        List<OrderItem> recentOrders = orders.stream()
                .flatMap(order -> orderItemRepository.findByOrd_no(order.getOrd_no()).stream())
                .limit(5)
                .collect(Collectors.toList());

        List<Point> recentPoints = pointRepository.findTop5ByMem_idOrderByP_dateDesc(mem_id);
        List<Review> recentReviews = reviewRepository.findTop5ByMem_idOrderByRev_rdateDesc(mem_id);
        List<Qna> recentQnas = qnaRepository.findTop5ByMem_idOrderByQ_rdateDesc(mem_id);

        // DTO로 변환
        return MyPageHomeDTO.builder()
                .orderCount((int) orderCount)
                .couponCount((int) couponCount)
                .pointTotal(1000)
                .qnaCount((int) qnaCount)
                .recentOrders(recentOrders.stream().map(this::convertToOrderItemDTO).collect(Collectors.toList()))
                .recentPoints(recentPoints.stream().map(point -> modelMapper.map(point, PointDTO.class)).collect(Collectors.toList()))
                .recentReviews(recentReviews.stream().map(this::convertToReviewDTO).collect(Collectors.toList()))
                .recentQnas(recentQnas.stream().map(qna -> modelMapper.map(qna, QnaDTO.class)).collect(Collectors.toList()))
                .build();
    }

    // 나의설정 페이지 데이터 조회
    public MemberDTO getMemberInfo(String memId) {
        Member member = memberRepository.findById(memId).orElse(null);
        return (member != null) ? modelMapper.map(member, MemberDTO.class) : null;
    }

    // DTO 변환을 위한 헬퍼 메서드
    private OrderItemDTO convertToOrderItemDTO(OrderItem item) {
        // 상품명 조회 로직 추가 필요
        // Product product = productRepository.findByProdNo(item.getProdNo()).orElse(null);
        return OrderItemDTO.builder()
                .item_no(item.getItem_no())
                .ord_no(item.getOrd_no())
                .prod_no(item.getProd_no())
                .item_name("상품명") // 임시
                .item_cnt(item.getItem_cnt())
                .item_stat(item.getItem_stat())
                .build();
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        // 상품명 조회 로직
        String prodName = "알 수 없는 상품";
        Optional<Product> productOptional = productRepository.findByProd_no(review.getProd_no());
        if (productOptional.isPresent()) {
            prodName = productOptional.get().getProd_name();
        }

        return ReviewDTO.builder()
                .rev_no(review.getRev_no())
                .prod_no(review.getProd_no())
                .mem_id(review.getMem_id())
                .prodName(prodName) // 추가된 필드에 값 할당
                .rev_content(review.getRev_content())
                .rev_rating(review.getRev_rating())
                .rev_rdate(review.getRev_rdate())
                .rev_img_path(review.getRev_img_path())
                .build();
    }
}