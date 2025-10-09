package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TERMS")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int t_no;

    private String t_terms;
    private String t_tax;
    private String t_finance;
    private String t_privacy;
    private String t_location;
}
