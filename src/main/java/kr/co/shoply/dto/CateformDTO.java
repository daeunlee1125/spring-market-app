package kr.co.shoply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CateformDTO {
    private List<Cate1DTO> mainCategories = new ArrayList<>();
    private List<Cate2DTO> subCategories =  new ArrayList<>();

}
