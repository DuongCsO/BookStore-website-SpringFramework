package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.BaseModel;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.BaseService;
import jakarta.persistence.MappedSuperclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@MappedSuperclass
public abstract class BaseServiceImpl<T extends BaseModel, R extends JpaRepository<T, Long>> implements BaseService<T> {
    protected R repository;

    @Autowired
    public BaseServiceImpl(R repository){
        this.repository=repository;
    }
    @Override
    public List<T> getAll() {
        List<T> list = repository.findAll().stream().filter(t -> t.isDeleted()==false).collect(Collectors.toList());
        return list;
    }
    @Override
    public Page<T> getByPage(int page, int pageSize, T t){
        Pageable pageable = PageRequest.of(page, pageSize,Sort.by("createdOn").descending());
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("deleted");
        String[] ignorePaths = fieldNames.toArray(new String[0]);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths(ignorePaths)
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<T> exampleSpec = Example.of(t, matcher);
        return repository.findAll(exampleSpec, pageable);
    }
    @Override
    public Page<T> searchByName(int page, int pageSize, T t){
        Pageable pageable = PageRequest.of(page, pageSize,Sort.by("createdOn").descending());
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("deleted");
        fieldNames.remove("name");
        String[] ignorePaths = fieldNames.toArray(new String[0]);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths(ignorePaths)
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<T> exampleSpec = Example.of(t, matcher);
        return repository.findAll(exampleSpec, pageable);
    }
    @Override
    public Page<T> searchById(int page, int pageSize, T t){
        Pageable pageable = PageRequest.of(page, pageSize,Sort.by("createdOn").descending());
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("deleted");
        fieldNames.remove("id");
        String[] ignorePaths = fieldNames.toArray(new String[0]);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths(ignorePaths)
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<T> exampleSpec = Example.of(t, matcher);
        return repository.findAll(exampleSpec, pageable);
    }
    @Override
    public T getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public T create(T t) {
        return repository.save(t);
    }

    @Override
    public T edit(Long id, T t) {
        if(repository.findById(id)!=null) return repository.save(t);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if(repository.findById(id)!=null) {
            T t = repository.findById(id).orElse(null);
            t.setDeleted(true);
            repository.save(t);
            return true;
        }
        return false;
    }
}
