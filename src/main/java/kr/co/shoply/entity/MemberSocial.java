package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER_SOCIAL")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_member_social")
    @SequenceGenerator(name = "seq_member_social", sequenceName = "SEQ_MEMBER_SOCIAL", allocationSize = 1)
    @Column(name = "MS_ID")
    private Long msId;

    @Column(name = "MEM_ID", length = 30)
    private String memId;

    @Column(name = "SOCIAL_TYPE", length = 20)
    private String socialType;  // google, naver, kakao

    @Column(name = "SOCIAL_ID", length = 100)
    private String socialId;

    @Column(name = "SOCIAL_EMAIL", length = 100)
    private String socialEmail;

    @Column(name = "MS_RDATE")
    private LocalDateTime msRdate;

    @PrePersist
    public void prePersist() {
        this.msRdate = LocalDateTime.now();
    }
}