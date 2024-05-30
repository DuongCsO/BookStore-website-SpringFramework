package com.example.doAnTotNghiep.DoAnTotNghiep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_order")
public class Order extends BaseModel {
    @ManyToOne
    @JoinColumn(name ="account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    private String orderCode;
    private String description;
    private long totalMoney;
    private byte status;
    private String address;
}
