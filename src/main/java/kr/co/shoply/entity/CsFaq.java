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
@Table(name = "CS_FAQ")
public class CsFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cs_faq_no")
    private int csFaqNo;

    @Column(name = "cs_faq_cate1")
    private String csFaqCate1;

    @Column(name = "cs_faq_cate2")
    private String csFaqCate2;

    @Column(name = "cs_faq_title")
    private String csFaqTitle;

    @Column(name = "cs_faq_content")
    private String csFaqContent;

    @Column(name = "cs_faq_hit")
    private int csFaqHit;

    @CreationTimestamp
    @Column(name = "cs_faq_rdate")
    private LocalDateTime csFaqRdate;

}
