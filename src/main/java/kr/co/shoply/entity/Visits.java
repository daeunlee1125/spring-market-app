package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Visits")
public class Visits {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int v_id;

    @CreationTimestamp
    LocalDateTime v_date;
}
