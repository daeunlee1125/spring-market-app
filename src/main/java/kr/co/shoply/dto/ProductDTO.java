package kr.co.shoply.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String prod_no;
    private int cate2_no;
    private String prod_name;
    private String prod_info;
    private String prod_company;
    private String mem_id;
    private int prod_price;
    private int prod_sale; // 할인율
    private int prod_deliv_price;
    private int prod_point;
    private int prod_stock;
    private int prod_sold;
    private int prod_hit;

    // 추가 필드
    private double avgRating; // 평균 리뷰 점수
    private int cntRating; // 리뷰 개수
    private int realPrice; // 실 판매 가격
    private int saleprice; // 총 할인 가격
    private int totalprice; // 총 주문 가격
    private int totaldeliv; // 총 주문 배송비
    
    private String f_name; // 이미지파일 경로
    private String f_rdate; // 상품 등록일 = 파일 등록일

    //my페이지 by jinwon
    private List<ProdOptionDTO> options;
    private List<ProFileDTO> files;
    public void setOptions(List<ProdOptionDTO> options) {
        this.options = options;
    }

    public void setFiles(List<ProFileDTO> files) {
        this.files = files;
    }
}
