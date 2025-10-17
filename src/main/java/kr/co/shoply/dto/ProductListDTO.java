package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDTO {
    private String prod_no;
    private String prod_name;
    private String prod_company;
    private String mem_id;
    private int prod_price;
    private int prod_point;
    private int prod_stock;
    private int prod_hit;

    private String f_name;



}
