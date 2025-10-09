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
@Table(name = "CATE1")
public class Cate1 {

    @Id
            // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cate1_no;

    private String cate1_name;

}
