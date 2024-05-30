package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Order;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService extends BaseService<Review> {
    List<Review> findByProductDetail(Long id);
    Page<Review> getPageByProductDetail(int page, int pageSize, Review t);
}
