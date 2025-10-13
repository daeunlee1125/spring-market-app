package kr.co.shoply.mapper;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.entity.CsFaq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CsFaqMapper {

    List<CsFaq> selectFaqList(@Param("cate1") String cate1,
                              @Param("cate2") String cate2);


    List<CsFaqDTO> selectAllFaqs();
    List<CsFaqDTO> selectFaqsByCate1(String cate1);
    List<CsFaqDTO> selectFaqsByCate1AndCate2(String cate1, String cate2);


    CsFaq selectFaq(int cs_faq_no);

    int insertFaq(CsFaq csFaq);

    int updateFaq(CsFaq csFaq);

    int deleteFaq(int cs_faq_no);

    int deleteFaqs(@Param("faqIds") List<Integer> faqIds);
}
