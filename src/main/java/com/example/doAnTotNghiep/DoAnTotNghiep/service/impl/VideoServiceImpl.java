package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Video;
import com.example.doAnTotNghiep.DoAnTotNghiep.repository.VideoRepository;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.VideoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl extends BaseServiceImpl<Video, VideoRepository> implements VideoService {
    public VideoServiceImpl(VideoRepository repository) {
        super(repository);
    }
    public List<Video> findByProductDetail(Long id){
        return repository.findAll().stream().filter(p -> p.isDeleted()==false&&p.getProductDetail().getId().equals(id)).collect(Collectors.toList());
    }
}
