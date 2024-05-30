package com.example.doAnTotNghiep.DoAnTotNghiep.controller.admin;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.*;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {
    private final OrderService orderService;
    private final PaymentMethodService paymentMethodService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    public AdminOrderController(OrderService orderService, PaymentMethodService paymentMethodService, OrderDetailService orderDetailService, ProductService productService) {
        this.orderService = orderService;
        this.paymentMethodService = paymentMethodService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
    }
    //BASE METHODS
    //pagination T list
    private <T extends BaseModel, R extends BaseService> String tManagement(Model model, int page, String retn, R serv, T t){
        int pageSize = 10;
        Page<T> tPage = serv.getByPage(page, pageSize,t);
        model.addAttribute("tPage", tPage);
        return retn;
    }
    //T unit detail
    private <T extends BaseModel, R extends BaseService> String tUnitDetail(Model model, Long tId, String retn, R serv){
        T t = (T) serv.getById(tId);
        model.addAttribute("tUnit", t);
        return retn;
    }
    //create new T form
    private <T> String createTForm(Model model,T tForm, String retn){
        model.addAttribute("tForm", tForm);
        return retn;
    }
    //post method create new T
    private <T extends BaseModel, R extends BaseService> String createT(T t, R serv, String tName, BindingResult result, Model model){
        if(result.hasErrors()) {
            model.addAttribute("tForm", t);
            return "/admin/product/create-"+tName+"-form";
        }
        serv.create(t);
        return "redirect:/admin/"+tName;
    }
    //edit T form
    private <T, R extends BaseService> String editTForm(Model model, Long tId, R serv, String retn){
        T tForm = (T) serv.getById(tId);
        model.addAttribute("tForm", tForm);
        return retn;
    }
    //post method edit T
    private <T extends BaseModel, R extends BaseService> String editT(Model model, Long tId, T t, R serv, String tName, BindingResult result){
        if(result.hasErrors()){
            model.addAttribute("tForm", t);
            return "/admin/product/edit-"+tName+"-form";
        }
        t.setCreatedOn(serv.getById(tId).getCreatedOn());
        t.setCreatedBy(serv.getById(tId).getCreatedBy());
        t.setDeleted(serv.getById(tId).isDeleted());
        serv.edit(tId, t);
        model.addAttribute("tForm", t);
        return "redirect:/admin/"+tName;
    }
    //post method delete T
    private <T extends BaseModel, R extends BaseService> String deleteT(Long id, R serv, RedirectAttributes redirectAttributes, String retn){
        if(serv.delete(id)){
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thành công đối tượng");
        } else{
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đối tượng để xóa");
        }
        return retn;
    }
    public void updateProduct(ProductDetail productDetail, int quantity){
        Long id = productDetail.getProduct().getId();
        Product p = productService.getById(id);
        p.setQuantity(p.getQuantity()-quantity);
        productService.edit(id, p);
    }
    //ORDER
    @GetMapping("/order")
    public String orderManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model,page,"/admin/order-management", orderService, new Order());
    }
    @PostMapping("/order/set-status/{id}/{status}")
    public String setStatusOrder(@PathVariable("status")byte status,
                                 @PathVariable("id") Long id,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page){
        if(status==0){
            List<OrderDetail> orderDetails = orderDetailService.findByOrder(id);
            for(OrderDetail c:orderDetails){
                updateProduct(c.getProductDetail(), c.getQuantity());
            }
        }
        Order order = orderService.getById(id);
        order.setStatus(status);
        orderService.edit(id,order);
        return tManagement(model,page,"/admin/order-management", orderService, new Order());
    }
    @GetMapping("/order/{id}")
    public String orderUnit(Model model, @PathVariable("id") Long id){
        List<OrderDetail> orderDetails= orderDetailService.findByOrder(id);
        model.addAttribute("orderDetails", orderDetails);
        return tUnitDetail(model, id, "/admin/order/order-unit", orderService);
    }
    @PostMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, orderService, redirectAttributes, "redirect:/admin/order");
    }

    //PAYMENT METHOD
    @GetMapping("/payment-method")
    public String paymentMethodManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model, page, "/admin/order/payment-method-list", paymentMethodService, new PaymentMethod());
    }
    @GetMapping("/payment-method/{id}")
    public String paymentMethodUnit(@PathVariable("id") Long id, Model model){
        return tUnitDetail(model, id, "/admin/order/payment-method-unit", paymentMethodService);
    }
    @GetMapping("/payment-method/create")
    public String createPaymentMethodForm(Model model){
        PaymentMethod paymentMethod = new PaymentMethod();
        return createTForm(model, paymentMethod, "/admin/order/create-payment-method-form");
    }

    @PostMapping("/payment-method/create")
    public String createPaymentMethod(@Valid @ModelAttribute("tForm") PaymentMethod paymentMethod, BindingResult result, Model model){
        return createT(paymentMethod,paymentMethodService,"payment-method", result, model);
    }
    @GetMapping("/payment-method/edit/{id}")
    public String editPaymentMethodForm(@PathVariable Long id, Model model){
        return editTForm(model, id, paymentMethodService,"/admin/order/edit-payment-method-form");
    }

    @PostMapping("/payment-method/edit/{id}")
    public String editPaymentMethod(@PathVariable Long id, @Valid @ModelAttribute("tForm") PaymentMethod paymentMethod, BindingResult result, Model model){
        return editT(model, id, paymentMethod, paymentMethodService, "payment-method", result);
    }
    @PostMapping("/payment-method/delete/{id}")
    public String deletePaymentMethod(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, paymentMethodService, redirectAttributes, "redirect:/admin/payment-method");
    }
}
