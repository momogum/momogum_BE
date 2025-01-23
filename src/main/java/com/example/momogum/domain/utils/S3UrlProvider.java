package com.example.momogum.domain.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class S3UrlProvider {

    /**
     * 기본/재미 등 카테고리에 해당하는 S3 URL 리스트를 반환하는 util 클래스
     * @param category 카테고리 이름 (예: "basic", "fun")
     * @return S3 이미지 URL 리스트
     */
    public List<String> getUrlsByCategory(String category) {

        // 실제 구현이 아닌, S3와 통신 후 URL 리스트를 반환하는 메서드라고 가정.
        return List.of(
                "https://example-bucket.s3.amazonaws.com/" + category + "/image1.jpg",
                "https://example-bucket.s3.amazonaws.com/" + category + "/image2.jpg"
        );
    }
}
