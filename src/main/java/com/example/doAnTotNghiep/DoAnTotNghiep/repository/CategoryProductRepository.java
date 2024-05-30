package com.example.doAnTotNghiep.DoAnTotNghiep.repository;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
}
