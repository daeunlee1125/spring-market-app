package kr.co.shoply.service;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.*;
import kr.co.shoply.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserCouponRepository userCouponRepository;
    private final PointRepository pointRepository;
    private final QnaRepository qnaRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MyPageHomeDTO getMyPageHomeData(String memId) {
        long orderCount = orderRepository.countByMem_id(memId);
        long couponCount = userCouponRepository.countByMem_id(memId);
        Integer pointTotal = Optional.ofNullable(pointRepository.getTotalPointsByMem_id(memId)).orElse(0);
        long qnaCount = qnaRepository.countByMem_id(memId);

        List<Order> recentOrdersEntity = orderRepository.findTop5ByMem_idOrderByOrd_dateDesc(memId);
        List<OrderItemDTO> recentOrders = recentOrdersEntity.stream()
                .flatMap(order -> orderItemRepository.findByOrd_no(order.getOrd_no()).stream()
                        .map(item -> {
                            OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
                            // OrderItemDTO에 ord_date 필드가 필요하며, Date 타입으로 변환 후 할당
                            dto.setOrd_date(order.getOrd_date());
                            return dto;
                        }))
                .limit(5)
                .collect(Collectors.toList());

        List<Point> recentPointsEntity = pointRepository.findTop5ByMem_idOrderByP_dateDesc(memId);
        List<PointDTO> recentPoints = recentPointsEntity.stream()
                .map(entity -> modelMapper.map(entity, PointDTO.class))
                .collect(Collectors.toList());

        List<Review> recentReviewsEntity = reviewRepository.findTop5ByMem_idOrderByRev_rdateDesc(memId);
        List<ReviewDTO> recentReviews = recentReviewsEntity.stream()
                .map(entity -> {
                    ReviewDTO dto = modelMapper.map(entity, ReviewDTO.class);
                    Optional<Product> product = productRepository.findByProd_no(entity.getProd_no());
                    product.ifPresent(p -> dto.setProdName(p.getProd_name()));
                    return dto;
                })
                .collect(Collectors.toList());

        List<Qna> recentQnasEntity = qnaRepository.findTop5ByMem_idOrderByQ_rdateDesc(memId);
        List<QnaDTO> recentQnas = recentQnasEntity.stream()
                .map(entity -> modelMapper.map(entity, QnaDTO.class))
                .collect(Collectors.toList());

        return MyPageHomeDTO.builder()
                .orderCount((int) orderCount)
                .couponCount((int) couponCount)
                .pointTotal(pointTotal)
                .qnaCount((int) qnaCount)
                .recentOrders(recentOrders)
                .recentPoints(recentPoints)
                .recentReviews(recentReviews)
                .recentQnas(recentQnas)
                .build();
    }

    public MemberDTO getMemberInfo(String memId) {
        Member member = memberRepository.findById(memId).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        return modelMapper.map(member, MemberDTO.class);
    }

    @Transactional
    public void updateMemberInfo(MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberDTO.getMem_id())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.updateInfo(memberDTO.getMem_email(), memberDTO.getMem_hp(), memberDTO.getMem_zip(), memberDTO.getMem_addr1(), memberDTO.getMem_addr2());
        memberRepository.save(member);
    }

    @Transactional
    public void changePassword(String memId, String newPassword) {
        Member member = memberRepository.findById(memId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
        memberRepository.save(member);
    }

    @Transactional
    public void withdrawMember(String memId) {
        Member member = memberRepository.findById(memId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        member.updateStatus("탈퇴");
        memberRepository.save(member);
    }

    @Transactional
    public void confirmOrder(Long itemNo, String memId) {
        OrderItem orderItem = orderItemRepository.findById(itemNo)
                .orElseThrow(() -> new IllegalArgumentException("주문 상품을 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderItem.getOrd_no())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        if (!order.getMem_id().equals(memId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        orderItem.setItem_stat(2);
        orderItemRepository.save(orderItem);

        Point point = Point.builder()
                .mem_id(memId)
                .p_type(1)
                .p_point(100)
                .p_info("상품구매확정")
                .p_date(LocalDateTime.now())
                .p_exp_date(LocalDateTime.now().plusYears(1))
                .build();
        pointRepository.save(point);
    }

    @Transactional
    public void writeReview(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        // LocalDateTime -> Date로 변환 후 할당
        Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        review.setRev_rdate(currentDate);
        reviewRepository.save(review);
    }

    @Transactional
    public void writeQna(QnaDTO qnaDTO) {
        Qna qna = modelMapper.map(qnaDTO, Qna.class);
        // LocalDateTime -> Date로 변환 후 할당
        Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        qna.setQ_rdate(currentDate);
        qnaRepository.save(qna);
    }
}