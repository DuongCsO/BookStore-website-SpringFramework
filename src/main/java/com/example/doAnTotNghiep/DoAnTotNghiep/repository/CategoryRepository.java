package com.example.doAnTotNghiep.DoAnTotNghiep.repository;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    long count();
}
