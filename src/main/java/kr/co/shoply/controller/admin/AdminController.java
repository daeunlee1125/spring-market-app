package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.AdInfoDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.service.SiteInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final SiteInfoService siteInfoService;

    @GetMapping({"/admin/", "admin/admin"})
    public String admin(Model model) {

        AdInfoDTO info = siteInfoService.getAdminInfos();

        info.setVisit(siteInfoService.getVisitCnt());
        info.setTodVisit(siteInfoService.getTodayCnt());
        info.setYesVisit(siteInfoService.getYesterdayCnt());
        model.addAttribute("info", info);

        List<CsNoticeDTO> notList = siteInfoService.getCsNotList();
        model.addAttribute("notList", notList);

        return "admin/admin";
    }
}
