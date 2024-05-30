package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    Page<Account> getByPage(int page, int pageSize);
    List<Account> getAll();
    Account getById(Long id);
    Account create(Account t);
    Account edit(Long id,Account t);
    boolean delete(Long id);
}
