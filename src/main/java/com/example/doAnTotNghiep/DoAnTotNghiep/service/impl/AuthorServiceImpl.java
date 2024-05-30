package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Author;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.AuthorRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl extends BaseServiceImpl<Author, AuthorRepository> implements AuthorService {
    @Autowired
    public AuthorServiceImpl(AuthorRepository repository) {
        super(repository);
    }
}
