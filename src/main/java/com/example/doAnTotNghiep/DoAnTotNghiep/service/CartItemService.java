package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.CartItem;

import java.util.List;

public interface CartItemService extends BaseService<CartItem> {
    List<CartItem> findByAccount(Long id);
}
