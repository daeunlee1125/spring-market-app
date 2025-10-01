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
@Table(name = "BANNER")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ban_no;

    private String ban_name;
    private String ban_img;
    private String ban_size;
    private String ban_bg_color;
    private int ban_location;
    private String ban_banner_link;
    private String ban_start_date;
    private String ban_end_date;
    private String ban_status;





}
