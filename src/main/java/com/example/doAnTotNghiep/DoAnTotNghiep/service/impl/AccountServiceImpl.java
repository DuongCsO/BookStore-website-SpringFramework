package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.AccountRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<Account> getByPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return accountRepository.findAll(pageable);
    }

    @Override
    public List<Account> getAll() {
        List<Account> list = accountRepository.findAll().stream().collect(Collectors.toList());
        return list;
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account create(Account t) {
        return accountRepository.save(t);
    }

    @Override
    public Account edit(Long id, Account t) {
        if(accountRepository.findById(id)!=null) return accountRepository.save(t);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if(accountRepository.findById(id)!=null) {
            Account account = accountRepository.findById(id).orElse(null);
            account.setStatus((byte)1);
            accountRepository.save(account);
            return true;
        }
        return false;
    }
}
