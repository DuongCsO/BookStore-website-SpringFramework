package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.ProductDetail;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.ProductDetailRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.ProductDetailService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductDetailServiceImpl extends BaseServiceImpl<ProductDetail, ProductDetailRepository> implements ProductDetailService {
    public ProductDetailServiceImpl(ProductDetailRepository repository) {
        super(repository);
    }

    @Override
    public List<ProductDetail> findByProduct(Long id) {
        return repository.findAll().stream().filter(p -> !p.isDeleted()&&p.getProduct().getId().equals(id)).collect(Collectors.toList());
    }
    @Override
    public Page<ProductDetail> searchByName(int page, int pageSize, ProductDetail t){
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdOn").descending());
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("deleted");
        fieldNames.remove("product");

        String[] ignorePaths = fieldNames.toArray(new String[0]);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths(ignorePaths)
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("product.name", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<ProductDetail> exampleSpec = Example.of(t, matcher);
        return repository.findAll(exampleSpec, pageable);
    }
}
