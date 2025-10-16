package kr.co.shoply.controller.admin;

import kr.co.shoply.dto.OrderDTO;
import kr.co.shoply.dto.PageRequestDTO;
import kr.co.shoply.dto.PageResponseDTO;
import kr.co.shoply.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping("/admin/order/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO pageResponseDTO = orderService.getOrders2(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        List<OrderDTO> details = orderService.OrderDetails2();
        model.addAttribute("details", details);

        return "admin/order/list";
    }
    @GetMapping("/admin/order/delivery")
    public String orderDelivery() {

        return "admin/order/delivery";
    }
}
