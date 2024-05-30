package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Video;

import java.util.List;

public interface VideoService extends BaseService<Video> {
    List<Video> findByProductDetail(Long id);
}
