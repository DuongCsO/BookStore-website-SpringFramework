package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.CategoryProduct;

import java.util.List;

public interface CategoryProductService extends BaseService<CategoryProduct>{
    List<CategoryProduct> findByProduct(Long id);
    List<CategoryProduct> findByCategory(Long id);
}
