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
@Setter
@Table(name = "CS_FAQ")
public class CsFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csFaqSeqGenerator")
    @SequenceGenerator(
            name = "csFaqSeqGenerator",
            sequenceName = "CS_FAQ_SEQ",
            allocationSize = 1
    )
    private int cs_faq_no;

    private String cs_faq_cate1;
    private String cs_faq_cate2;
    private String cs_faq_title;
    private String cs_faq_content;
    private int cs_faq_hit;

    @CreationTimestamp
    private LocalDateTime cs_faq_rdate;

}
