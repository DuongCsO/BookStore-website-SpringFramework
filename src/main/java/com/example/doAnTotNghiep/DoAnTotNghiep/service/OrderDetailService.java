package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.OrderDetail;

import java.util.List;

public interface OrderDetailService extends BaseService<OrderDetail>{
    List<OrderDetail> findByOrder(Long id);
    List<OrderDetail> findByCreatedOn(int day);
}
