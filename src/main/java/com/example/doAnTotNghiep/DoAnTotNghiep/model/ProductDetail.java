package com.example.doAnTotNghiep.DoAnTotNghiep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product_detail")
public class ProductDetail extends BaseModel{
    private long price;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
