package kr.co.practice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CS_NOTICE")
public class CsNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cs_no;

    private String cs_type;
    private String cs_title;
    private String cs_content;
    private int cs_hit;

    @CreationTimestamp
    private LocalDateTime cs_rdate;
}
