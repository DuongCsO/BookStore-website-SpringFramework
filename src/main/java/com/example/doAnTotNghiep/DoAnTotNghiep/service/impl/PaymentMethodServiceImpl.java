package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.PaymentMethod;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.PaymentMethodRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.PaymentMethodService;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl extends BaseServiceImpl<PaymentMethod, PaymentMethodRepository> implements PaymentMethodService {
    public PaymentMethodServiceImpl(PaymentMethodRepository repository) {
        super(repository);
    }
}
