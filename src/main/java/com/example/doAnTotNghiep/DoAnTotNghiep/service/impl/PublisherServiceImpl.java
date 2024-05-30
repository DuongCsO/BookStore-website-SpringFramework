package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Publisher;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.PublisherRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.PublisherService;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl extends BaseServiceImpl<Publisher, PublisherRepository> implements PublisherService {
    public PublisherServiceImpl(PublisherRepository repository) {
        super(repository);
    }
}
