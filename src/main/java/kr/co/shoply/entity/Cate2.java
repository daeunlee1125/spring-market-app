package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CATE2")
public class Cate2 {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cate2_no;

    private int cate1_no;
    private String cate2_name;

}
