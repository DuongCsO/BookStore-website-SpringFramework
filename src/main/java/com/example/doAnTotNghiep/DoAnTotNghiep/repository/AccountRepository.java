package com.example.doAnTotNghiep.DoAnTotNghiep.repository;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUsername(String username);

}
