package kr.co.shoply.controller;

import kr.co.shoply.dto.ArticleDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.ArticleService;
import kr.co.shoply.service.CompanyRecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CompanyController {

    private final CompanyRecruitService companyRecruitService;
    private final ArticleService articleService;

    @GetMapping("/company/recruit")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("response", companyRecruitService.list(pageRequestDTO));
        return "company/recruit";
    }

    /** 상세 */
    @GetMapping("/company/recruit_list")
    public String recruit() {
        return "company/recruit_list";
    }

    /** 상세 */
    @GetMapping("/company/recruit_view/{recNo}")
    public String read(@PathVariable Integer recNo, Model model) {
        model.addAttribute("dto", companyRecruitService.read(recNo));
        return "company/recruit_view";
    }

    @GetMapping("/company/culture")
    public String culture(){
        return "company/culture";
    }

    @GetMapping("/company/index")
    public String index(Model model) {
        model.addAttribute("recentArticles", articleService.getRecentArticles());
        return "company/index";
    }

    @GetMapping("/company/media")
    public String media(){
        return "company/media";
    }

    @GetMapping("/company/story")
    public String story(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<ArticleDTO> response = articleService.list(pageRequestDTO);
        model.addAttribute("response", response);
        return "company/story";
    }

    @GetMapping("/company/story_list/{artNo}")
    public String view(@PathVariable Integer artNo, Model model) {
        model.addAttribute("dto", articleService.read(artNo));
        return "company/story_list";
    }
}
