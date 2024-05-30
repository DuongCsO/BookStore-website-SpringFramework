package com.example.doAnTotNghiep.DoAnTotNghiep.service;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Video;

import java.io.IOException;
import java.util.List;

public interface YoutubeService {
    List<Video> fetchData(String str) throws IOException, InterruptedException;
    List<Video> fetchDatan(String str) throws IOException, InterruptedException;
}
