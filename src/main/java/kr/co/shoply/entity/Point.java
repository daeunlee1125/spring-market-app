package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "POINT")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int p_no;

    private String mem_id;
    private int p_type;
    private int p_point;
    private String p_info;

    @CreationTimestamp
    private LocalDateTime p_date;
    private LocalDateTime p_exp_date;

    private String ord_no;
}
