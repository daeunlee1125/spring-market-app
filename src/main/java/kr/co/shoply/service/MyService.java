package kr.co.shoply.service;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.*;
import kr.co.shoply.mapper.MyProductMapper;
import kr.co.shoply.mapper.ProductMapper;
import kr.co.shoply.mapper.ReviewMapper;
import kr.co.shoply.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

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
    private final ReviewMapper reviewMapper;
    private final ProdOptionRepository prodOptionRepository; // 옵션
    private final ProFileRepository proFileRepository;       // 파일
    private final MyProductMapper myproductMapper;


    @Transactional(readOnly = true)
    public ProductDTO getProduct3(String prodNo) {
        ProductDTO product = myproductMapper.selectProduct(prodNo); // MyProductMapper 사용
        product.setOptions(getProductOption3(prodNo));
        product.setFiles(getProductFiles(prodNo));
        return product;
    }

    // 상품 파일
    @Transactional(readOnly = true)
    public List<ProdOptionDTO> getProductOption3(String prodNo) {
        List<ProdOptionDTO> options = myproductMapper.selectOption(prodNo);
        options.forEach(opt -> opt.setOptValList(Arrays.asList(opt.getOpt_val().split("\\s*,\\s*"))));
        return options;
    }

    @Transactional(readOnly = true)
    public List<ProFileDTO> getProductFiles(String prodNo) {
        return myproductMapper.selectFiles(prodNo);
    }


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

        List<Point> recentPointsEntity = pointRepository.findByMem_idOrderByP_dateDesc(memId);
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

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByMemId(String memId) {
        List<Review> reviewEntities = reviewRepository.findByMem_idOrderByRev_rdateDesc(memId);
        return reviewEntities.stream()
                .map(entity -> {
                    ReviewDTO dto = modelMapper.map(entity, ReviewDTO.class);
                    productRepository.findByProd_no(entity.getProd_no())
                            .ifPresent(p -> dto.setProdName(p.getProd_name()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserCouponDTO> getUserCouponsByMemId(String memId) {
        // 네이티브 쿼리로 Object[] 리스트 가져오기
        List<Object[]> results = userCouponRepository.findUserCouponsNative(memId);

        // Object[] -> UserCouponDTO 변환
        return results.stream().map(row -> {
            UserCouponDTO dto = new UserCouponDTO();

            dto.setCp_no((String) row[0]);
            dto.setCp_code((String) row[1]);
            dto.setMem_id((String) row[2]);

            // java.sql.Timestamp 또는 java.sql.Date 모두 처리 가능하도록 수정
            if (row[3] != null) {
                if (row[3] instanceof java.sql.Timestamp) {
                    dto.setCp_issued_date(new Date(((java.sql.Timestamp) row[3]).getTime()));
                } else if (row[3] instanceof java.sql.Date) {
                    dto.setCp_issued_date(new Date(((java.sql.Date) row[3]).getTime()));
                }
            }

            if (row[4] != null) {
                if (row[4] instanceof java.sql.Timestamp) {
                    dto.setCp_used_date(new Date(((java.sql.Timestamp) row[4]).getTime()));
                } else if (row[4] instanceof java.sql.Date) {
                    dto.setCp_used_date(new Date(((java.sql.Date) row[4]).getTime()));
                }
            }

            dto.setCp_stat(row[5] != null ? ((Number) row[5]).intValue() : null);

            dto.setCp_type(row[6] != null ? ((Number) row[6]).intValue() : 0);
            dto.setCp_value(row[7] != null ? ((Number) row[7]).intValue() : 0);
            dto.setCp_name((String) row[8]);

            // cp_issuer_name은 쿼리에서 제거되었으므로 주석 처리
            // dto.setCp_issuer_name((String) row[9]);

            // cp_exp_date 처리
            if (row[9] != null) {
                if (row[9] instanceof java.sql.Timestamp) {
                    dto.setCp_exp_date(new Date(((java.sql.Timestamp) row[9]).getTime()));
                } else if (row[9] instanceof java.sql.Date) {
                    dto.setCp_exp_date(new Date(((java.sql.Date) row[9]).getTime()));
                }
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public List<PointDTO> getPointHistory(String memId) {
        List<Point> points = pointRepository.findByMem_idOrderByP_dateDesc(memId);

        // 날짜를 "yyyy-MM-dd" 형식으로 포맷하는 포맷터 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return points.stream()
                .map(point -> {
                    PointDTO dto = modelMapper.map(point, PointDTO.class);

                    // LocalDateTime을 String으로 변환하여 DTO에 설정
                    dto.setP_date(point.getP_date().format(formatter));
                    dto.setP_exp_date(point.getP_exp_date().format(formatter));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long itemNo, String memId) {
        OrderItem orderItem = orderItemRepository.findById(itemNo)
                .orElseThrow(() -> new IllegalArgumentException("주문 상품을 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderItem.getOrd_no())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        if (!order.getMem_id().equals(memId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        orderItem.setItem_stat(0); // 취소 상태 코드
        orderItemRepository.save(orderItem);
    }
    @Transactional(readOnly = true)
    public List<OrderItemDTO> getOrdersByMemId(String memId) {
        // 회원 주문 조회
        List<Order> orders = orderRepository.findTop5ByMem_idOrderByOrd_dateDesc(memId);

        // Order -> OrderItemDTO 변환
        return orders.stream()
                .flatMap(order -> orderItemRepository.findByOrd_no(order.getOrd_no()).stream()
                        .map(item -> {
                            OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
                            dto.setOrd_date(order.getOrd_date());
                            return dto;
                        }))
                .collect(Collectors.toList());
    }



}