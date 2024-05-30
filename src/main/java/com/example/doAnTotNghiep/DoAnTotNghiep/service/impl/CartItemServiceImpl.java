package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Account;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.CartItem;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.CartItemRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, CartItemRepository> implements CartItemService {
    public CartItemServiceImpl(CartItemRepository repository) {
        super(repository);
    }
    @Override
    public List<CartItem> findByAccount(Long id) {
        return repository
                .findAll()
                .stream()
                .filter(c -> c.isDeleted()==false&&c.getAccount().getId().equals(id))
                .collect(Collectors.toList());
    }
}
