package com.example.doAnTotNghiep.DoAnTotNghiep.controller.customer;

import com.example.doAnTotNghiep.DoAnTotNghiep.config.CustomUserDetails;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.*;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class CartController {
    private final CartItemService cartItemService;
    private final ProductDetailService productDetailService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final PaymentMethodService paymentMethodService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderDetailService orderDetailService;

    public CartController(CartItemService cartItemService,
                          ProductDetailService productDetailService,
                          AccountService accountService,
                          OrderService orderService,
                          PaymentMethodService paymentMethodService,
                          ProductService productService,
                          CategoryService categoryService, OrderDetailService orderDetailService) {
        this.cartItemService = cartItemService;
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.orderService = orderService;
        this.paymentMethodService = paymentMethodService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderDetailService = orderDetailService;
    }
    @PostMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account user = userDetails.getUser();;
        List<CartItem> cartItems = cartItemService.findByAccount(user.getId());
        CartItem cartItem = null;
        for(CartItem c: cartItems){
            if(c.getProductDetail().getId().equals(id)) cartItem=c;
        }
        if(cartItem==null){
            cartItem = new CartItem();
            cartItem.setAccount(user);
            cartItem.setQuantity(1);
            cartItem.setProductDetail(productDetailService.getById(id));
            cartItemService.create(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemService.edit(cartItem.getId(), cartItem);
        }
        return "redirect:/";
    }
    @GetMapping("/cart")
    public String cartDetail(Model model){
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getUser();
        List<CartItem> cartItems = cartItemService.findByAccount(account.getId());
        model.addAttribute("cartItems", cartItems);
        long total = 0;
        for(CartItem cartItem:cartItems){
            total+=cartItem.getQuantity()*cartItem.getProductDetail().getPrice();
        }
        model.addAttribute("total", total);
        return "/customer/cart";
    }

    @PostMapping("/cart/up/{id}")
    public String upQuantity(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getUser();
        CartItem cartItem = cartItemService.getById(id);
        ProductDetail productDetail=cartItem.getProductDetail();
        if (cartItem.getQuantity()<productDetail.getProduct().getQuantity()-1){
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemService.edit(id, cartItem);
        }
        return "redirect:/cart";
    }
    @PostMapping("/cart/down/{id}")
    public String downQuantity(@PathVariable("id") Long id){
        CartItem cartItem = cartItemService.getById(id);
        if(cartItem.getQuantity()>0){
            cartItem.setQuantity(cartItem.getQuantity()-1);
            cartItemService.edit(id, cartItem);
        }
        return "redirect:/cart";
    }
    @PostMapping("/cart/delete/{id}")
    public String deleteQuantity(@PathVariable("id") Long id){
        cartItemService.delete(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutForm(Model model){
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        Order order = new Order();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getUser();
        model.addAttribute("account", account);
        model.addAttribute("order", order);
        List<CartItem> cartItems = cartItemService.findByAccount(account.getId());
        model.addAttribute("cartItems", cartItems);
        long total = 0;
        for(CartItem c: cartItems){
            total+=c.getProductDetail().getPrice()*c.getQuantity();
        }
        model.addAttribute("total", total);
        List<PaymentMethod> paymentMethods = paymentMethodService.getAll();
        model.addAttribute("paymentMethods", paymentMethods);
        return "/customer/checkout";
    }
    @PostMapping("/order/create")
    public String checkout(@ModelAttribute("order") Order order, Model model){
        try{
            List<Category> categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Account account = userDetails.getUser();
            order.setAccount(account);
            order.setOrderCode(UUID.randomUUID().toString());
            List<CartItem> cartItems = cartItemService.findByAccount(account.getId());
            long total = 0;
            List<OrderDetail> orderDetails = new ArrayList<>();
            for(CartItem c: cartItems){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setQuantity(c.getQuantity());
                orderDetail.setProductDetail(c.getProductDetail());
                total+=c.getProductDetail().getPrice()*c.getQuantity();
                orderDetails.add(orderDetail);
                cartItemService.delete(c.getId());
            }
            order.setStatus((byte)1);
            order.setTotalMoney(total);
            orderService.create(order);
            for(OrderDetail o:orderDetails) orderDetailService.create(o);
            return "/customer/checkout-success";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/error/404";
    }
    @GetMapping("/checkout/list")
    public String getListCheckout(Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 10;
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getUser();
        Order order = new Order();
        order.setAccount(account);
        Page<Order> tPage = orderService.getPageByAccount(page, pageSize, order);
        model.addAttribute("tPage", tPage);
        return "/customer/checkout-list";
    }
    @GetMapping("/checkout/item/{id}")
    public String getCheckoutItem(Model model, @PathVariable("id")Long id){
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        List<OrderDetail> orderDetails = orderDetailService.findByOrder(id);
        model.addAttribute("orderItems", orderDetails);
        return "/customer/checkout-item";
    }
}
