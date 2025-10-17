package kr.co.shoply.dto;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseDTO<T> {

    private List<T> dtoList;

    private String cate;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String searchType;
    private String keyword;

    // ✅ QNA 필터링 상태 유지를 위한 필드 추가
    private String cate1;
    private String cate2;


    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total){

        this.cate = pageRequestDTO.getCate();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        // 0일 때 1로 수정
        int last = (total > 0) ? (int) (Math.ceil(total / (double) size)) : 1;
        this.end = Math.min(end, last);
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

        this.searchType = pageRequestDTO.getSearchType();
        this.keyword = pageRequestDTO.getKeyword();

        // qna
        this.cate1 = pageRequestDTO.getCate1();
        this.cate2 = pageRequestDTO.getCate2();
    }






}