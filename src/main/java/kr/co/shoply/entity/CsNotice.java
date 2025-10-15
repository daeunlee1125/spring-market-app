package kr.co.shoply.entity;

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
    @Column(name="cs_no")
    private Integer csNo;

    @Column(name = "cs_type")
    private String csType;

    @Column(name = "cs_title")
    private String csTitle;

    @Column(name = "cs_content")
    private String csContent;

    @Column(name = "cs_hit")
    private int csHit;

    @CreationTimestamp
    @Column(name = "cs_rdate")
    private LocalDateTime csRdate;
}
