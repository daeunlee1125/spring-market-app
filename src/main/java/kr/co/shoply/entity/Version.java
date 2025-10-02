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
@Table(name = "VERSION")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ver_no;

    private String ver_version;
    private String ver_description;
    private String ver_writer;

    @CreationTimestamp
    private LocalDateTime ver_rdate;

}
