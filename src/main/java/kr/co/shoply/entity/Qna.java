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
    private int q_no;

    private String mem_id;
    private String q_cate1;
    private String q_cate2;
    private String q_channel;
    private String q_title;
    private String q_content;
    private String q_channel;

    @CreationTimestamp
    private LocalDateTime q_rdate;
    
    private String q_reply;
    private String q_comment;
}
