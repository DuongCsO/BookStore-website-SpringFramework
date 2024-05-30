package com.example.doAnTotNghiep.DoAnTotNghiep.repository;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
