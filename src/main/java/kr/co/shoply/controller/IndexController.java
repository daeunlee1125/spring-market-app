package kr.co.shoply.controller;

import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final SiteInfoService siteInfoService;

    @GetMapping({"/", "/index"})
    public String index() {
        siteInfoService.logVisit();
        return "index";
    }
}
