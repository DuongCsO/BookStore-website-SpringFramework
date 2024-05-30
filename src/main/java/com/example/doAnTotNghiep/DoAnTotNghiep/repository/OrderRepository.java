package com.example.doAnTotNghiep.DoAnTotNghiep.repository;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    long count();
}
