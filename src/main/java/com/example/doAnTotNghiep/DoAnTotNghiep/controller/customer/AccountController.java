package com.example.doAnTotNghiep.DoAnTotNghiep.controller.customer;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.UserRole;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginForm(){
        return "/login-form";
    }
    @GetMapping("/register")
    public String registerForm(Model model){
        Account account = new Account();
        model.addAttribute("user", account);
        return "/register-form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole(UserRole.USER);
        accountService.create(account);
        return "redirect:/login";
    }
}
