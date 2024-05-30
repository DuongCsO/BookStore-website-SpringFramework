package com.example.doAnTotNghiep.DoAnTotNghiep.config;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Account account;

    public CustomUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserRole role = account.getRole(); // Assume UserRole is the enum type
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())); // Create SimpleGrantedAuthority from enum value
        return authorities;
    }

    public Account getUser(){return account;}

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
