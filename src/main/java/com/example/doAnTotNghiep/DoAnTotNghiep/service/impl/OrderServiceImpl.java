package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Order;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.OrderRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.OrderService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, OrderRepository> implements OrderService {
    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }
    @Override
    public Page<Order> getPageByAccount(int page, int pageSize, Order t){
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdOn").descending());
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("deleted");
        fieldNames.remove("account");
        String[] ignorePaths = fieldNames.toArray(new String[0]);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths(ignorePaths)
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("account", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Order> exampleSpec = Example.of(t, matcher);
        return repository.findAll(exampleSpec, pageable);
    }
}
