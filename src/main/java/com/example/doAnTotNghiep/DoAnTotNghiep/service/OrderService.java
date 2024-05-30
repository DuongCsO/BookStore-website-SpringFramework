package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Order;
import org.springframework.data.domain.Page;

public interface OrderService extends BaseService<Order> {
    Page<Order> getPageByAccount(int page, int pageSize, Order t);
}
