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
@Table(name = "MEMBER")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String mem_id;

    private String mem_pass;
    private String mem_name;
    private String mem_gen;
    private String mem_hp;
    private String mem_email;
    private int mem_level;
    private String mem_stat;

    @CreationTimestamp
    private LocalDateTime mem_rdate;
    private LocalDateTime mem_lastLogin;

    private String mem_zip;
    private String mem_addr1;
    private String mem_addr2;
}
