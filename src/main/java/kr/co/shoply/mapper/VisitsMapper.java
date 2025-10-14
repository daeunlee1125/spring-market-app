package kr.co.shoply.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface VisitsMapper {
    public void insertVisit();
    public int visitCount(LocalDate date);
    public int visitCount2();
}
