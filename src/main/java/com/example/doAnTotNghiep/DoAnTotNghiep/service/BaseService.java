package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.BaseModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService<T extends BaseModel> {
    List<T> getAll();
    Page<T> getByPage(int page, int pageSize, T t);
    Page<T> searchByName(int page, int pageSize, T t);
    Page<T> searchById(int page, int pageSize, T t);
    T getById(Long id);
    T create(T t);
    T edit(Long id,T t);
    boolean delete(Long id);
}
