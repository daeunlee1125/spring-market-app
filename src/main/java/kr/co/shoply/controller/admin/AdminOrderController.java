package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.CopyrightDTO;
import kr.co.shoply.dto.OrderDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.OrderService;
import kr.co.shoply.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;
    private final VersionService versionService;

    @GetMapping("/admin/order/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO pageResponseDTO = orderService.getOrders2(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        List<OrderDTO> details = orderService.OrderDetails2();
        model.addAttribute("details", details);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/order/list";
    }

    @PostMapping("/admin/order/list")
    public String list(OrderDTO orderDTO) {

        orderService.setDelvs(orderDTO);

        return "redirect:/admin/order/list";
    }

    @GetMapping("/admin/order/delivery")
    public String orderDelivery(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = orderService.getDelivs2(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        List<OrderDTO> delvDtls = orderService.delivDetails2();
        model.addAttribute("delvDtls", delvDtls);

        CopyrightDTO copyrightDTO = versionService.getCopyright3();
        model.addAttribute("copyrightDTO", copyrightDTO);

        return "admin/order/delivery";
    }


}
