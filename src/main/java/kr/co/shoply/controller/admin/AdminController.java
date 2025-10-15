package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.*;
import kr.co.shoply.service.Cate1Service;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Console;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final SiteInfoService siteInfoService;
    private final Cate1Service cate1Service;

    @GetMapping({"/admin/", "admin/admin"})
    public String admin(Model model) {

        AdInfoDTO info = siteInfoService.getAdminInfos();

        info.setVisit(siteInfoService.getVisitCnt());
        info.setTodVisit(siteInfoService.getTodayCnt());
        info.setYesVisit(siteInfoService.getYesterdayCnt());

        List<Chart1DTO> chart = siteInfoService.getChartInfo();
        List<Cate1DTO> chart2 = cate1Service.getCate1Chart();
        log.info("chart2={}", chart2);

        model.addAttribute("info", info);
        model.addAttribute("chart", chart);
        model.addAttribute("chart2", chart2);

        List<CsNoticeDTO> notList = siteInfoService.getCsNotList();
        model.addAttribute("notList", notList);

        List<QnaDTO> qnaList = siteInfoService.getQnaList();
        model.addAttribute("qnaList", qnaList);

        return "admin/admin";
    }
}
