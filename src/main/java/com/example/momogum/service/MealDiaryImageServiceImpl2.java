package com.example.momogum.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryImageRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class MealDiaryImageServiceImpl2 {

    @PersistenceContext
    private EntityManager entityManager;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryImageRepository mealDiaryImageRepository;



    @Override
    public List<String> uploadImages(List<MultipartFile> files, String dirName, Long mealDiaryId) {

        List<MealDiaryImage> images = new ArrayList<>();

        try {
            images = files.parallelStream()
                    .map(file -> uploadImage(dirName, file))
                    .collect(Collectors.toList());

            mealDiaryImageRepository.saveAll(images);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload images", e);
        }

        return images.stream()
                .map(MealDiaryImage::getImageLink)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteImage(String fileName) throws FileNotFoundException {
        MealDiaryImage image = mealDiaryImageRepository.findByFileName(fileName).orElseThrow(
                () -> new FileNotFoundException("Image not found: " + fileName));

        // S3에서 이미지 삭제
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image from S3: " + e.getMessage(), e);
        }

        // 데이터베이스에서 이미지 레코드 삭제
        mealDiaryImageRepository.delete(image);
    }

    @Override
    public String findImageByFileName(String fileName) {

        MealDiaryImage image = mealDiaryImageRepository.findByFileName(fileName).orElseThrow(
                () -> new RuntimeException("Image not found: " + fileName));

        return ImageDTO.ImageResponseDTO.builder()
                .fileName(image.getFileName())
                .imagePath(image.getImagePath())
                .build();
    }


    @Override
    public List<ImageDTO.ImageResponseDTO> findImagesByFeedId(Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("Feed not found"));

        return feed.getImages().stream()
                .map(image -> ImageDTO.ImageResponseDTO.builder()
                        .imagePath(image.getImagePath())
                        .fileName(image.getFileName())
                        .build())
                .collect(Collectors.toList());
    }

    private MealDiaryImage uploadImage(String dirName, MultipartFile file) {

        String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        String originalFileName = file.getOriginalFilename();

        try {
            log.info("Uploading image: {}");

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
            log.info("Image uploaded successfully: {}");
        } catch (AmazonServiceException e) {
            throw new RuntimeException("S3 service error: " + e.getMessage(), e);
        } catch (SdkClientException e) {
            throw new RuntimeException("S3 client error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Image upload error: " + e.getMessage(), e);
        }

        String imagePath = amazonS3.getUrl(bucket, fileName).toString();

        return MealDiaryImage.builder()
                .imageLink(imagePath)
                .fileName(fileName)
                .imageName(originalFileName)
                .build();
    }

    private Feed findFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(
                () -> new NotFoundException("Feed not found"));
    }

    private UserEntity findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username));
    }
}

}
