package kr.co.shoply.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MEM_SELLER")
public class MemSeller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String mem_id;

    private String corp_name;
    private String corp_reg_hp;
    private String corp_tel_hp;
    private String corp_fax;
}
