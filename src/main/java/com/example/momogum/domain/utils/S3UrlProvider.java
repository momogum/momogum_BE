package com.example.momogum.domain.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class S3UrlProvider {

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3UrlProvider(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    /**
     * S3에서 특정 카테고리의 이미지 파일 리스트 가져와 URL 리스트 반환
     *
     * @param category 카테고리 이름 (예: "basic", "fun", "event")
     * @return S3 이미지 URL 리스트
     */
    public List<String> getUrlsByCategory(String category) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(category + "/");

        ListObjectsV2Result result = amazonS3.listObjectsV2(request);

        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .map(key -> "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key)
                .collect(Collectors.toList());
    }

    /**
     * 모든 카테고리의 이미지 URL을 반환
     */
    public List<String> getAllUrls() {
        List<String> categories = List.of("basic", "fun", "event");

        return categories.stream()
                .flatMap(category -> getUrlsByCategory(category).stream())
                .collect(Collectors.toList());
    }
}