package com.example.doAnTotNghiep.DoAnTotNghiep.service.impl;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.Video;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.YoutubeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubeServiceImpl implements YoutubeService {
//    HttpClient client = HttpClient.newHttpClient();
//    HttpRequest request = HttpRequest.newBuilder()
//            .uri(URI.create("https://api.example.com/data"))
//            .build();
//
//    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.body());
    private final HttpClient client;
    @Value("${youtube.key}")
    private String youtubeKey;

    public YoutubeServiceImpl() {
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public List<Video> fetchData(String str) throws IOException, InterruptedException {
        String encodedStr = URLEncoder.encode(str, "UTF-8");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://youtube.googleapis.com/youtube/v3/search?part=snippet&relevanceLanguage=vi&maxResults=3&q="
                        +encodedStr+"&key="+youtubeKey))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println("Response Body: " + responseBody);

        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        List<Video> videos = new ArrayList<>();
        JsonNode itemsNode = rootNode.path("items");
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                JsonNode idNode = itemNode.path("id");
                JsonNode snippetNode = itemNode.path("snippet");

                Video video = new Video();
                video.setVideoId(idNode.path("videoId").asText());
                video.setTitle(snippetNode.path("title").asText());
                video.setDescription(snippetNode.path("description").asText());
                video.setThumbnailUrl(snippetNode.path("thumbnails").path("high").path("url").asText());
                video.setChannelTitle(snippetNode.path("channelTitle").asText());
                video.setPublishTime(snippetNode.path("publishTime").asText());
                videos.add(video);
            }
        }
        return videos;
    }

    @Override
    public List<Video> fetchDatan(String str) throws IOException, InterruptedException {
        String encodedStr = URLEncoder.encode(str, "UTF-8");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://youtube.googleapis.com/youtube/v3/search?part=snippet&relevanceLanguage=vi&maxResults=8&q="
                        +encodedStr+"&key="+youtubeKey))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println("Response Body: " + responseBody);

        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        List<Video> videos = new ArrayList<>();
        JsonNode itemsNode = rootNode.path("items");
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                JsonNode idNode = itemNode.path("id");
                JsonNode snippetNode = itemNode.path("snippet");

                Video video = new Video();
                video.setVideoId(idNode.path("videoId").asText());
                video.setTitle(snippetNode.path("title").asText());
                video.setDescription(snippetNode.path("description").asText());
                video.setThumbnailUrl(snippetNode.path("thumbnails").path("high").path("url").asText());
                video.setChannelTitle(snippetNode.path("channelTitle").asText());
                video.setPublishTime(snippetNode.path("publishTime").asText());
                videos.add(video);
            }
        }
        return videos;
    }
}

