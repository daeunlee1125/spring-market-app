package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "RECRUIT")
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rec_no;

    private String rec_title;
    private String rec_department;
    private String rec_experience;
    private String rec_job_type;
    private String rec_start_time;
    private String rec_end_date;
    private String rec_status;
    private String rec_content;
    private String rec_qualification;
    private String rec_welfare;

    @CreationTimestamp
    private LocalDateTime rec_rdate;
}
