package kr.co.shoply.mapper;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PointMapper {
    public int selectPoint3(String mem_id);
    void insertUsedPoint3(String mem_id, int p_type, int p_point, String p_info);
}
