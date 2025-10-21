package kr.co.shoply.service;

import kr.co.shoply.dto.ArticleDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.entity.Article;
import kr.co.shoply.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<ArticleDTO> getRecentArticles() {
        Page<Article> page = articleRepository.findRecent(PageRequest.of(0, 5));
        return page.getContent().stream().map(this::entityToDto).toList();
    }

    /** 목록 (카드 그리드) – 기본 6개씩 */
    @Transactional(readOnly = true)
    public PageResponseDTO<ArticleDTO> list(PageRequestDTO req) {
        int page = Math.max(1, req.getPg());
        int size = (req.getSize() > 0) ? req.getSize() : 6; // 카드라 6 추천
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Article> pageData = articleRepository.fetchAll(pageable);

        List<ArticleDTO> dtoList = pageData.getContent().stream()
                .map(this::entityToDto)
                .toList();

        return PageResponseDTO.<ArticleDTO>builder()
                .pageRequestDTO(req)
                .dtoList(dtoList)
                .total((int) pageData.getTotalElements())
                .build();
    }

    /** 상세 */
    @Transactional(readOnly = true)
    public ArticleDTO read(Integer artNo) {
        Article a = articleRepository.findOneByNo(artNo)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. no=" + artNo));
        return entityToDto(a);
    }

    /* ===== Mapping ===== */

    private ArticleDTO entityToDto(Article a) {
        ArticleDTO dto = new ArticleDTO();
        dto.setArt_article_no(a.getArt_article_no());
        dto.setArt_title(a.getArt_title());
        dto.setArt_sub_title(a.getArt_sub_title());
        dto.setArt_content(a.getArt_content());

        if (a.getArt_rdate() != null) {
            dto.setArt_rdate(a.getArt_rdate().toString());
        }
        return dto;
    }
}
