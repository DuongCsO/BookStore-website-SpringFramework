package com.example.doAnTotNghiep.DoAnTotNghiep.controller.admin;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.UserRole;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminAccountController {
    private final AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    //ACCOUNT
    @GetMapping("/account")
    public String accountManagement(Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 10;
        Page<Account> tPage = accountService.getByPage(page, pageSize);
        model.addAttribute("tPage", tPage);

        return "/admin/account-management";
    }
    @PostMapping("/account/set-admin/{id}")
    public String setAdmin(@PathVariable("id") Long id){
        Account account = accountService.getById(id);
        account.setRole(UserRole.ADMIN);
        accountService.edit(id, account);
        return "redirect:/admin/account";
    }

    @PostMapping("/account/set-user/{id}")
    public String setUser(@PathVariable("id") Long id){
        Account account = accountService.getById(id);
        account.setRole(UserRole.USER);
        accountService.edit(id, account);
        return "redirect:/admin/account";
    }
    @PostMapping("/account/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        Account account = accountService.getById(id);
        accountService.delete(id);
        return "redirect:/admin/account";
    }
}
