package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.ProductDetail;

import java.util.List;

public interface ProductDetailService extends BaseService<ProductDetail> {
    List<ProductDetail> findByProduct(Long id);
}
