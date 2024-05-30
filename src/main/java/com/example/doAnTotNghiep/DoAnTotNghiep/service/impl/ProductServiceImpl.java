package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Product;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.ProductRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, ProductRepository> implements ProductService {
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){super(productRepository);}
}
