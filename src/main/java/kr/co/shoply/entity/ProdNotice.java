package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prod_notice")
public class ProdNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int not_no;
    private String prod_no;
    private String not_name;
    private String not_val;
}
