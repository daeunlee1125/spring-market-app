package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class ReviewDTO {


    private int rev_no;
    private String prod_no;
    private String mem_id;
    private String rev_content;
    private int rev_rating;
    private String rev_rdate;
    private String rev_img_path;



}
