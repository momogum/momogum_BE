package com.example.momogum.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryImageRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hibernate.query.sqm.tree.SqmNode.log;


@Service
@RequiredArgsConstructor
public class MealDiaryImageServiceImpl implements MealDiaryImageService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryImageRepository mealDiaryImageRepository;



    @Override
    public String upload(MultipartFile multipartFile, String dirName, Long mealDiaryId) throws IOException {

        MealDiary mealDiary = getMealDiary(mealDiaryId);

        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = dirName + "/" + uniqueFileName;
        File uploadFile = convert(multipartFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        saveFileMetadata(originalFileName, fileName, uploadImageUrl, mealDiary);

        return uploadImageUrl; // 단축된 URL 반환
    }






    @Override
    public File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                throw new ImageHandler(ErrorStatus.IMAGE_CONVERT_ERROR);
            }
            return convertFile;
        }
        throw new ImageHandler(ErrorStatus.IMAGE_CONVERT_ERROR);
    }


    @Override
    public String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }


    @Override
    public void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
        } else {
            throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR);
        }
    }


    @Override
    public void saveFileMetadata(String originalFileName, String uniqueFileName,
                                 String uploadImageUrl, MealDiary mealDiary) {

        MealDiaryImage newMealDiaryImage = MealDiaryImage.builder()
                .imageLink(uploadImageUrl)
                .fileName(uniqueFileName)
                .imageName(originalFileName)
                .mealDiary(mealDiary)
                .build();

        mealDiaryImageRepository.save(newMealDiaryImage);
    }


    @Override
    public byte[] download(Long mealDiaryId) throws IOException {

        MealDiary mealDiary = getMealDiary(mealDiaryId);

        List<MealDiaryImage> byMealDiaryImages = mealDiaryImageRepository.findByMealDiary(mealDiary);
        List<String> uniqueFileNames = byMealDiaryImages.stream()
                .map(MealDiaryImage::getFileName)
                .distinct()
                .toList();

        for (String uniqueFileName : uniqueFileNames) {
            try {
                S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, uniqueFileName));
                try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                    return inputStream.readAllBytes();
                }
            } catch (AmazonS3Exception e) {
                throw new ImageHandler(ErrorStatus.IMAGE_DOWNLOAD_ERROR);
            } catch (IOException e) {
                // 파일 스트림 처리 중 발생한 예외 처리
                throw new ImageHandler(ErrorStatus.IMAGE_DOWNLOAD_ERROR);
            }
        }

        throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
    }



    // memberId로 이미지 조회
    @Override
    public byte[] downloadImage(Long mealDiaryId) throws IOException {


        MealDiary mealDiary = getMealDiary(mealDiaryId);

        List<MealDiaryImage> byMealDiaryImages = mealDiaryImageRepository.findByMealDiary(mealDiary);
        List<String> uniqueFileNames = byMealDiaryImages.stream()
                .map(MealDiaryImage::getFileName)
                .distinct()
                .toList();

        for (String uniqueFileName : uniqueFileNames) {
            try {
                S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, uniqueFileName));
                try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                    return inputStream.readAllBytes();
                }
            } catch (AmazonS3Exception e) {
                throw new ImageHandler(ErrorStatus.IMAGE_DOWNLOAD_ERROR);
            } catch (IOException e) {
                // 파일 스트림 처리 중 발생한 예외 처리
                throw new ImageHandler(ErrorStatus.IMAGE_DOWNLOAD_ERROR);
            }
        }

        throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
    }






    @Transactional
    @Override
    public void deleteFile(Long mealDiaryId) throws FileNotFoundException {
        try {
            // MealDiary 엔티티 조회
            MealDiary mealDiary = getMealDiary(mealDiaryId);

            // MealDiary에 연결된 이미지 조회
            List<MealDiaryImage> byMealDiaryImages = mealDiaryImageRepository.findByMealDiary(mealDiary);

            // 이미지 ID 중복 제거
            List<Long> mealDiaryIds = byMealDiaryImages.stream()
                    .map(MealDiaryImage::getId)
                    .distinct()
                    .toList();

            // 이미지 ID를 기반으로 실제 이미지 조회
            List<MealDiaryImage> images = mealDiaryIds.stream()
                    .map(id -> mealDiaryImageRepository.findById(id)
                            .orElseThrow(() -> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND)))
                    .toList();

            // S3 및 DB에서 이미지 삭제
            for (MealDiaryImage image : images) {
                String uniqueFileName = image.getFileName();

                if (uniqueFileName != null) {
                    // AWS S3에서 파일 삭제
                    amazonS3.deleteObject(bucket, uniqueFileName);

                    // MealDiary와 MealDiaryImage 간의 연결 해제
                    mealDiary.getMealDiaryImages().remove(image);
                    image.setMealDiary(null);

                    // 로컬 DB에서 MealDiaryImage 삭제
                    mealDiaryImageRepository.delete(image);

                    // 명시적으로 flush 호출
                    entityManager.flush();
                    // 1차 캐시 클리어
                    entityManager.clear();

                    log.info("Successfully deleted MealDiaryImage with ID: {}");
                } else {
                    log.error("파일을 찾을 수 없습니다: ID {}");
                }
            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류가 발생했습니다: ", e);
            throw new IllegalArgumentException("파일 삭제 중 오류가 발생했습니다.");
        }
    }


    @Override
    public String updateProfileImage(MultipartFile newImageFile, String dirName, Long mealDiaryId) throws IOException {
        // 유저 찾기
        MealDiary mealDiary = getMealDiary(mealDiaryId);

        // 기존 프로필 이미지 삭제
        deleteFile(mealDiaryId);

        // 새로운 이미지 업로드
        String originalFileName = newImageFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");
        String fileName = dirName + "/" + uniqueFileName;
        File uploadFile = convert(newImageFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        // 새로운 메타데이터 저장
        saveFileMetadata(originalFileName, fileName, uploadImageUrl, mealDiary);

        return uploadImageUrl;
    }















    // 밥일기 찾는 메서드
    public MealDiary getMealDiary(Long mealDiaryId){
        return mealDiaryRepository.findById(mealDiaryId)
                .orElseThrow(()-> new IllegalArgumentException("밥일기를 찾지 못했습니다"));
    }
}
