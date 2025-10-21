package kr.co.shoply.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface PointMapper {
    public int selectPoint3(String mem_id);
    void insertUsedPoint3(
            @Param("mem_id") String mem_id,
            @Param("p_type") int p_type,
            @Param("p_point") int p_point,
            @Param("p_info") String p_info,
            @Param("ord_no") String ord_no
    );
}
