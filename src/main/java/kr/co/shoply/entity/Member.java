package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MEMBER")
public class Member {
    @Id
    private String mem_id;
    private String mem_pass;
    private String mem_name;
    private String mem_gen;
    private String mem_hp;
    private String mem_email;
    private int mem_level;
    private String mem_stat;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date mem_rdate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date mem_lastLogin;

    private String mem_zip;
    private String mem_addr1;
    private String mem_addr2;
    private String mem_rank;

    // MyService에서 호출하는 메서드들을 직접 구현
    public void updateInfo(String memEmail, String memHp, String memZip, String memAddr1, String memAddr2) {
        this.mem_email = memEmail;
        this.mem_hp = memHp;
        this.mem_zip = memZip;
        this.mem_addr1 = memAddr1;
        this.mem_addr2 = memAddr2;
    }

    // 회원수정에 필요한 거
    private String mem_note;

    public void updatePassword(String newPassword) {
        this.mem_pass = newPassword;
    }

    public void updateStatus(String status) {
        this.mem_stat = status;
    }
}