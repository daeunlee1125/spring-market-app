package kr.co.shoply.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegisterDTO {
    // 기본정보
    private String prod_name;
    private String prod_info;
    private String prod_company;
    private Integer prod_price;
    private Integer prod_sale;
    private Integer prod_point;
    private Integer prod_stock;
    private Integer prod_deliv_price;

    // 분류
    private Integer cate1_no;
    private Integer cate2_no;

    // 파일
    private MultipartFile file1;
    private MultipartFile file2;
    private MultipartFile file3;
    private MultipartFile file4;

    // 옵션 (동적으로 여러 개 가능)
    private List<String> optNames;
    private List<String> optVals;

    // 고시정보
    private String not_val1;
    private String not_val2;
    private String not_val3;
    private String not_val4;
    private String not_val5;
}
