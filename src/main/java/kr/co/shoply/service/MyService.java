package kr.co.shoply.service;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.*;
import kr.co.shoply.mapper.MyProductMapper;
import kr.co.shoply.mapper.ReviewMapper;
import kr.co.shoply.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl; // ⬅️ 이 부분을 추가하세요
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    private final ProdOptionRepository prodOptionRepository;
    private final ProFileRepository proFileRepository;
    private final MyProductMapper myproductMapper;
    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public ProductDTO getProduct3(String prodNo) {
        ProductDTO product = myproductMapper.selectProduct(prodNo);
        product.setOptions(getProductOption3(prodNo));
        product.setFiles(getProductFiles(prodNo));
        return product;
    }

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

        // ===== 최근 주문내역 (5개) =====
        List<Order> recentOrdersEntity = orderRepository.findTop5ByMem_idOrderByOrd_dateDesc(memId);
        List<OrderItemDTO> recentOrders = recentOrdersEntity.stream()
                .flatMap(order -> orderItemRepository.findByOrd_no(order.getOrd_no()).stream()
                        .map(item -> {
                            OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
                            dto.setOrd_date(order.getOrd_date());
                            return dto;
                        }))
                .limit(5)
                .collect(Collectors.toList());

        // ===== 포인트 내역 (5개) - 수정됨 =====
        List<Point> recentPointsEntity = pointRepository.findByMem_idOrderByP_dateDesc(memId);
        List<PointDTO> recentPoints = recentPointsEntity.stream()
                .limit(5)  // ✅ 5개로 제한
                .map(entity -> {
                    PointDTO dto = modelMapper.map(entity, PointDTO.class);

                    // ✅ 주문번호 명시적 설정
                    dto.setOrd_no(entity.getOrd_no());

                    // ✅ 날짜 포맷팅
                    if (entity.getP_date() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        dto.setP_date(entity.getP_date().format(formatter));
                    }
                    if (entity.getP_exp_date() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        dto.setP_exp_date(entity.getP_exp_date().format(formatter));
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        // ===== 상품평 (5개) =====
        List<Review> recentReviewsEntity = reviewRepository.findTop5ByMem_idOrderByRev_rdateDesc(memId);
        List<ReviewDTO> recentReviews = recentReviewsEntity.stream()
                .map(entity -> {
                    ReviewDTO dto = modelMapper.map(entity, ReviewDTO.class);
                    Optional<Product> product = productRepository.findByProd_no(entity.getProd_no());
                    product.ifPresent(p -> dto.setProdName(p.getProd_name()));
                    return dto;
                })
                .collect(Collectors.toList());

        // ===== 나의 문의 (5개) =====
        List<Qna> recentQnasEntity = qnaRepository.findTop5ByMem_idOrderByQ_rdateDesc(memId);
        List<QnaDTO> recentQnas = recentQnasEntity.stream()
                .map(entity -> {
                    QnaDTO dto = modelMapper.map(entity, QnaDTO.class);
                    if (entity.getQ_rdate() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        dto.setQ_rdate(
                                entity.getQ_rdate().toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .format(formatter)
                        );
                    }
                    return dto;
                })
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

        orderItem.setItem_stat(4);
        orderItemRepository.save(orderItem);

        Optional<Product> productOpt = productRepository.findByProd_no(orderItem.getProd_no());
        int pointAmount = 0;

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            pointAmount = (int) (product.getProd_price() * orderItem.getItem_cnt() * 0.1);
        }

        Point point = Point.builder()
                .mem_id(memId)
                .p_type(1)
                .p_point(pointAmount)
                .p_info("상품구매확정")
                .p_date(LocalDateTime.now())
                .p_exp_date(LocalDateTime.now().plusDays(7))
                .ord_no(String.valueOf(orderItem.getOrd_no()))  // ✅ 주문번호 추가
                .build();
        pointRepository.save(point);
    }

    @Transactional
    public void requestReturn(Long itemNo, String memId, String reason) {
        OrderItem orderItem = orderItemRepository.findById(itemNo)
                .orElseThrow(() -> new IllegalArgumentException("주문 상품을 찾을 수 없습니다."));
        Order order = orderRepository.findById(orderItem.getOrd_no())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        if (!order.getMem_id().equals(memId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        orderItem.setItem_stat(6);
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public void requestExchange(Long itemNo, String memId, String reason) {
        OrderItem orderItem = orderItemRepository.findById(itemNo)
                .orElseThrow(() -> new IllegalArgumentException("주문 상품을 찾을 수 없습니다."));
        Order order = orderRepository.findById(orderItem.getOrd_no())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        if (!order.getMem_id().equals(memId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        orderItem.setItem_stat(5);
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public ReviewDTO writeReview(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        review.setRev_rdate(currentDate);

        if (reviewDTO.getRev_files() != null && !reviewDTO.getRev_files().isEmpty()) {
            review.setRev_img_path(reviewDTO.getRev_files().get(0));
        }

        reviewRepository.save(review);
        ReviewDTO savedDTO = modelMapper.map(review, ReviewDTO.class);
        productRepository.findByProd_no(review.getProd_no())
                .ifPresent(p -> savedDTO.setProdName(p.getProd_name()));
        return savedDTO;
    }

    @Transactional
    public void writeQna(QnaDTO qnaDTO) {
        Qna qna = modelMapper.map(qnaDTO, Qna.class);
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
        List<Object[]> results = userCouponRepository.findUserCouponsNative(memId);
        return results.stream().map(row -> {
            UserCouponDTO dto = new UserCouponDTO();

            // 기본 정보
            dto.setCp_no((String) row[0]);
            dto.setCp_code((String) row[1]);
            dto.setMem_id((String) row[2]);

            // 발급일 (row[3])
            if (row[3] != null) {
                try {
                    if (row[3] instanceof java.sql.Timestamp) {
                        dto.setCp_issued_date(new Date(((java.sql.Timestamp) row[3]).getTime()));
                    } else if (row[3] instanceof java.sql.Date) {
                        dto.setCp_issued_date(new Date(((java.sql.Date) row[3]).getTime()));
                    } else if (row[3] instanceof String) {
                        // TO_DATE 함수가 String을 반환할 수 있음
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        dto.setCp_issued_date(sdf.parse((String) row[3]));
                    }
                } catch (Exception e) {
                    log.error("발급일 파싱 실패", e);
                }
            }

            // 사용일 (row[4])
            if (row[4] != null) {
                try {
                    if (row[4] instanceof java.sql.Timestamp) {
                        dto.setCp_used_date(new Date(((java.sql.Timestamp) row[4]).getTime()));
                    } else if (row[4] instanceof java.sql.Date) {
                        dto.setCp_used_date(new Date(((java.sql.Date) row[4]).getTime()));
                    } else if (row[4] instanceof String) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        dto.setCp_used_date(sdf.parse((String) row[4]));
                    }
                } catch (Exception e) {
                    log.error("사용일 파싱 실패", e);
                }
            }

            // 쿠폰 상태 (row[5])
            dto.setCp_stat(row[5] != null ? ((Number) row[5]).intValue() : null);

            // 쿠폰 타입 (row[6])
            dto.setCp_type(row[6] != null ? ((Number) row[6]).intValue() : 0);

            // 쿠폰 가치 (row[7])
            dto.setCp_value(row[7] != null ? ((Number) row[7]).intValue() : 0);

            // 쿠폰명 (row[8])
            dto.setCp_name((String) row[8]);

            // 최소 구매금액 (row[9])
            dto.setCp_min_price(row[9] != null ? ((Number) row[9]).intValue() : 0);

            // ✅ 유효기간 (row[10]) - 수정됨!
            if (row[10] != null) {
                try {
                    if (row[10] instanceof java.sql.Timestamp) {
                        dto.setCp_exp_date(new Date(((java.sql.Timestamp) row[10]).getTime()));
                    } else if (row[10] instanceof java.sql.Date) {
                        dto.setCp_exp_date(new Date(((java.sql.Date) row[10]).getTime()));
                    } else if (row[10] instanceof java.time.LocalDate) {
                        dto.setCp_exp_date(java.sql.Date.valueOf((java.time.LocalDate) row[10]));
                    } else if (row[10] instanceof String) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        dto.setCp_exp_date(sdf.parse((String) row[10]));
                    }
                } catch (Exception e) {
                    log.error("유효기간 파싱 실패", e);
                }
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public List<PointDTO> getPointHistory(String memId) {
        List<Point> points = pointRepository.findByMem_idOrderByP_dateDesc(memId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return points.stream()
                .map(point -> {
                    PointDTO dto = modelMapper.map(point, PointDTO.class);

                    // 날짜 포맷팅
                    if (point.getP_date() != null) {
                        dto.setP_date(point.getP_date().format(formatter));
                    }
                    if (point.getP_exp_date() != null) {
                        dto.setP_exp_date(point.getP_exp_date().format(formatter));
                    } else {
                        dto.setP_exp_date("-");
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ✅ 상태명 변환 헬퍼 메서드
    private String getItemStatName(int stat) {
        switch(stat) {
            case 0: return "결제완료";
            case 1: return "배송준비";
            case 2: return "배송중";
            case 3: return "배송완료";
            case 4: return "구매확정";
            case 5: return "교환신청";
            case 6: return "반품신청";
            default: return "알수없음";
        }
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

        orderItem.setItem_stat(0);
        orderItemRepository.save(orderItem);
    }

    // PageImpl을 사용하여 DTO 페이지를 반환하는 메서드로 수정
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> getOrdersByMemId(String memId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByMem_id(memId, pageable);
        List<OrderItemDTO> items = orderPage.getContent().stream()
                .flatMap(order -> orderItemRepository.findByOrd_no(order.getOrd_no()).stream()
                        .map(item -> {
                            OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
                            dto.setOrd_date(order.getOrd_date());
                            return dto;
                        }))
                .collect(Collectors.toList());
        return new PageImpl<>(items, pageable, orderPage.getTotalElements());
    }


    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsByMemIdPaged(String memId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByMem_id(memId, pageable);

        return reviewPage.map(review -> {
            ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
            productRepository.findByProd_no(review.getProd_no())
                    .ifPresent(p -> dto.setProdName(p.getProd_name()));
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public List<QnaDTO> getRecentQnas(String memId) {
        List<Qna> recentQnasEntity = qnaRepository.findTop5ByMem_idOrderByQ_rdateDesc(memId);

        return recentQnasEntity.stream()
                .map(entity -> {
                    QnaDTO dto = modelMapper.map(entity, QnaDTO.class);
                    if (entity.getQ_rdate() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        dto.setQ_rdate(
                                entity.getQ_rdate().toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .format(formatter)
                        );
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BannerDTO getBannerByNo(int banNo) {
        Banner banner = bannerRepository.findById(banNo).orElse(null);
        if (banner != null) {
            return modelMapper.map(banner, BannerDTO.class);
        }
        return null;
    }


    // MyService.java에 추가
    @Transactional(readOnly = true)
    public void debugPointData(String memId) {
        List<Point> points = pointRepository.findByMem_idOrderByP_dateDesc(memId);
        log.info("===== 포인트 데이터 디버그 =====");
        for (Point point : points) {
            log.info("p_no: {}, ord_no: {}, p_point: {}, p_info: {}",
                    point.getP_no(),
                    point.getOrd_no(),
                    point.getP_point(),
                    point.getP_info());
        }
        log.info("===== 디버그 종료 =====");
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getOrderDetail(Long ordNo, Long itemNo, String memId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 주문 정보 조회 (ordNo를 String으로 변환)
            Order order = orderRepository.findById(String.valueOf(ordNo))
                    .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

            // 권한 확인 (자신의 주문만 볼 수 있도록)
            if (!order.getMem_id().equals(memId)) {
                throw new IllegalArgumentException("권한이 없습니다.");
            }

            // 주문 상품 정보 조회
            OrderItem orderItem = orderItemRepository.findById(itemNo)
                    .orElseThrow(() -> new IllegalArgumentException("주문상품을 찾을 수 없습니다."));

            // 상품 정보 조회
            ProductDTO product = getProduct3(orderItem.getProd_no());

            // 회원 정보 조회
            Member member = memberRepository.findById(order.getMem_id())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

            // 결과 맵 구성
            result.put("ordNo", ordNo);
            result.put("itemNo", itemNo);
            result.put("prodName", product.getProd_name());
            result.put("prodPrice", product.getProd_price());
            result.put("itemCnt", orderItem.getItem_cnt());
            result.put("itemStat", orderItem.getItem_stat());
            result.put("totalPrice", product.getProd_price() * orderItem.getItem_cnt());
            result.put("discount", 0);

            // 날짜 포맷팅 (java.util.Date 사용)
            if (order.getOrd_date() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String ordDate = sdf.format(order.getOrd_date());
                result.put("ordDate", ordDate);
            } else {
                result.put("ordDate", "");
            }

            // 상품 이미지
            if (product.getFiles() != null && !product.getFiles().isEmpty()) {
                result.put("prodImage", product.getFiles().get(0).getF_name());
            }

            // 배송 정보 (회원 정보)
            result.put("memName", member.getMem_name());
            result.put("memHp", member.getMem_hp());
            result.put("memZip", member.getMem_zip());
            result.put("memAddr1", member.getMem_addr1());
            result.put("memAddr2", member.getMem_addr2());
            result.put("deliveryMemo", "");

            return result;

        } catch (IllegalArgumentException e) {
            log.error("주문상세 조회 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("주문상세 조회 중 예기치 않은 오류", e);
            throw new RuntimeException("주문상세 조회 중 오류가 발생했습니다.");
        }
    }

}