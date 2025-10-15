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
@Table(name = "QNA")
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_no")
    private int qNo;

    @Column(name = "mem_id")
    private String memId;

    @Column(name = "q_cate1")
    private String qCate1;

    @Column(name = "q_cate2")
    private String qCate2;

    @Column(name = "q_channel")
    private String qChannel;

    @Column(name = "q_title")
    private String qTitle;

    @Column(name = "q_content")
    private String qContent;

    @CreationTimestamp
    @Column(name = "q_rdate")
    private LocalDateTime qrDate;
    public LocalDateTime getQrDate() { return qrDate; }

    @Column(name = "q_reply")
    private String qReply;

    @Column(name = "q_comment")
    private String qComment;
}