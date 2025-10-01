package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prod_option")
public class ProdOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int opt_no;
    private String prod_no;
    private String opt_name;
    private String opt_val;
}
