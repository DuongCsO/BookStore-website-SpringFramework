package com.example.doAnTotNghiep.DoAnTotNghiep.repository;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}