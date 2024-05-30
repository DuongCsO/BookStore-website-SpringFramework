package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.config.CustomUserDetails;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.AccountRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.AccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    public CustomUserDetailServiceImpl(AccountService accountService, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account =accountRepository.findByUsername(username);
        if(account==null){
            throw new UsernameNotFoundException("Username or Password not found");
        }
        return new CustomUserDetails(account);
    }
}
