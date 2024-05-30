package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.CategoryProduct;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.OrderDetail;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.OrderDetailRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetail, OrderDetailRepository> implements OrderDetailService {
    public OrderDetailServiceImpl(OrderDetailRepository repository) {
        super(repository);
    }
    @Override
    public List<OrderDetail> findByOrder(Long id){
        return repository.findAll().stream().filter(p -> p.isDeleted()==false&&p.getOrder().getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetail> findByCreatedOn(int day) {
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(day);
        return repository.findAll()
                .stream()
                .filter(orderDetail -> orderDetail.getCreatedOn().getYear()==daysAgo.getYear()
                        &&orderDetail.getCreatedOn().getDayOfYear()==daysAgo.getDayOfYear())
                .collect(Collectors.toList());
    }
}
