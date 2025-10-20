package kr.co.shoply.repository;

import kr.co.shoply.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    /** 목록: 최신순(번호 desc) – 정렬은 JPQL에 고정 */
    @Query("select a from Article a order by a.art_article_no desc")
    Page<Article> fetchAll(Pageable pageable);

    /** 단건 조회 */
    @Query("select a from Article a where a.art_article_no = :no")
    Optional<Article> findOneByNo(@Param("no") Integer no);

    @Query("select a from Article a order by a.art_rdate desc")
    Page<Article> findRecent(Pageable pageable);
}