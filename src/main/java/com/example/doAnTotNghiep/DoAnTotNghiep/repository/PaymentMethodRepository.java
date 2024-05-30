package com.example.doAnTotNghiep.DoAnTotNghiep.repository;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
