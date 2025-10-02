package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rev_no;

    private String prod_no;
    private String mem_id;
    private String rev_content;
    private int rev_rating;
    private String rev_rdate;
    private String rev_img_path;






}
