package kr.co.shoply.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    private String prod_no;
    private int cate2_no;
    private String prod_name;
    private String prod_info;
    private String prod_company;
    private String mem_id;
    private int prod_price;
    private int prod_sale;
    private int prod_deliv_price;
    private int prod_point;
    private int prod_stock;
    private int prod_sold;
    private int prod_hit;

}
