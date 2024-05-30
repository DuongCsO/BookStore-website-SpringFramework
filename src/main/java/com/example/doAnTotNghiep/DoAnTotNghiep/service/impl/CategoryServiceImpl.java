package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Category;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.CategoryRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category, CategoryRepository> implements CategoryService {
    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }
}
