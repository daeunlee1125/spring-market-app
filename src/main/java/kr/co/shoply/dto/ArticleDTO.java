package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {

    private int art_article_no;
    private String art_title;
    private String art_sub_title;
    private String art_content;
    private String art_rdate;

    public String getArt_rdate() {
        return art_rdate.substring(2, 10).replace("T", " ");
    }
}
