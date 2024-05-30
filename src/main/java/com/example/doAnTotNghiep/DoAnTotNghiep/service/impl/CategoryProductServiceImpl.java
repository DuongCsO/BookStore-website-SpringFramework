package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.CategoryProduct;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.CategoryProductRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.CategoryProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryProductServiceImpl extends BaseServiceImpl<CategoryProduct, CategoryProductRepository> implements CategoryProductService {
    public CategoryProductServiceImpl(CategoryProductRepository repository) {
        super(repository);
    }
    @Override
    public List<CategoryProduct> findByProduct(Long id){
        return repository.findAll().stream().filter(p -> p.isDeleted()==false&&p.getProduct().getId().equals(id)).collect(Collectors.toList());
    }
    @Override
    public List<CategoryProduct> findByCategory(Long id){
        return repository.findAll().stream().filter(p -> p.isDeleted()==false&&p.getCategory().getId().equals(id)).collect(Collectors.toList());
    }
}
