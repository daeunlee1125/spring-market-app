package kr.co.shoply.mapper;

import kr.co.shoply.dto.Cate1DTO;
import kr.co.shoply.dto.Cate2DTO;
import kr.co.shoply.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Cate1Mapper {

    public List<Cate1DTO> chartCate1();
}
