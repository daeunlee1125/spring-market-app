package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pro_file")
public class ProFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int f_no;
    private int prod_no;
    private String f_name;
    private String f_rdate;
}
