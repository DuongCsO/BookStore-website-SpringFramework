package com.example.doAnTotNghiep.DoAnTotNghiep.controller.admin;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.OrderDetail;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.CategoryService;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.OrderDetailService;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.OrderService;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminReportController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminReportController(OrderService orderService, OrderDetailService orderDetailService, ProductService productService, CategoryService categoryService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.categoryService = categoryService;
    }
    @GetMapping("/report")
    public String report(Model model){
        long totalProduct = productService.getAll().stream().count();
        model.addAttribute("totalProduct", totalProduct);
        long totalOrder = orderService.getAll().stream().count();
        model.addAttribute("totalOrder", totalOrder);
        long totalCate = categoryService.getAll().stream().count();
        model.addAttribute("totalCate", totalCate);
        return "/admin/report/report";
    }
    @GetMapping("/report/revenue")
    public String report(Model model, @RequestParam(defaultValue = "7")int day){
        Map<String, Integer> revenueData = new HashMap<>();
        int totalM=0;
        for(int i=day-1; i>=0; i--){
            List<OrderDetail> orderDetails = orderDetailService.findByCreatedOn(i);
            String date = LocalDateTime.now().minusDays(i).format(DateTimeFormatter.BASIC_ISO_DATE);
            int total=0;
            for(OrderDetail o:orderDetails){
                total+=o.getQuantity()*o.getProductDetail().getPrice();
            }
            totalM+=total;
            revenueData.put(date,total);
            System.out.println(date+"----"+total);
        }
        model.addAttribute("total", totalM);
        model.addAttribute("revenueData", revenueData);
        return "/admin/report-management";
    }
}
