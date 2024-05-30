package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Order;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.OrderDetail;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.Review;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.ReviewRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.ReviewService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl extends BaseServiceImpl<Review, ReviewRepository> implements ReviewService {
    public ReviewServiceImpl(ReviewRepository repository) {
        super(repository);
    }
    @Override
    public List<Review> findByProductDetail(Long id){
        return repository.findAll().stream().filter(p -> p.isDeleted()==false&&p.getProductDetail().getId().equals(id)).collect(Collectors.toList());
    }
    @Override
    public Page<Review> getPageByProductDetail(int page, int pageSize, Review t){
        Pageable pageable = PageRequest.of(page, pageSize);
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("deleted");
        fieldNames.remove("productDetail");
        String[] ignorePaths = fieldNames.toArray(new String[0]);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths(ignorePaths)
                .withIgnorePaths("productDetail.product")
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("productDetail.id", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Review> exampleSpec = Example.of(t, matcher);
        return repository.findAll(exampleSpec, pageable);
    }
}
