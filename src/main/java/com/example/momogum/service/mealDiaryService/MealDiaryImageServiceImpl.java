package com.example.momogum.service.mealDiaryService;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryImageRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class MealDiaryImageServiceImpl implements MealDiaryImageService {


    private final AmazonS3 amazonS3;
    private final EntityManager entityManager;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryImageRepository mealDiaryImageRepository;



    /**
     * 이미지를 업로드하는 메서드입니다
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    @Override
    public List<String> uploadImages(List<MultipartFile> files, String dirName, Long mealDiaryId) {

        List<MealDiaryImage> images = new ArrayList<>();
        MealDiary mealDiary = findMealDiary(mealDiaryId);

        try {
            images = files.parallelStream()
                    .map(file -> uploadImage(dirName, file,mealDiary))
                    .collect(Collectors.toList());

            mealDiaryImageRepository.saveAll(images);

        } catch (Exception e) {
            throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
        }

        return images.stream()
                .map(MealDiaryImage::getImageLink)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void deleteImage(Long mealDiaryId) throws FileNotFoundException {

        // MealDiaryId에 해당하는 이미지 리스트 조회
        List<String> findImagesByMealDiary = findImagesByMealId(mealDiaryId);

        MealDiary mealDiary = findMealDiary(mealDiaryId);

        List<MealDiaryImage> byMealDiary = mealDiaryImageRepository.findByMealDiary(mealDiary);

        if (findImagesByMealDiary == null || findImagesByMealDiary.isEmpty()) {
            throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
        }

        // 엔티티 매니저를 사용하여 flush() 호출
        entityManager.flush();  // 영속성 컨텍스트의 상태를 DB에 강제로 반영

        // S3에서 이미지 삭제 및 데이터베이스 레코드 삭제
        for (MealDiaryImage mealDiaryImage : byMealDiary) {
            try {
                // S3에서 이미지 삭제
                amazonS3.deleteObject(bucket, mealDiaryImage.getFileName());


                /**
                 *
                 * JPA의 영속성 컨텍스트안에 삭제해야하는 엔티티와 연관된 엔티티가 존재함
                 * 그렇기 때문에 삭제 쿼리가 발생하지 않는 오류 발견
                 *
                 * -> 엔티티 간의 연관관계를 끊어줌
                 *
                 * */
                mealDiaryImage.getMealDiary().removeMealDiaryImage(mealDiaryImage);
                mealDiaryImage.removeMealDiary();



                // 데이터베이스에서 이미지 레코드 삭제
                mealDiaryImageRepository.delete(mealDiaryImage);
            } catch (Exception e) {
                throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR);
            }
        }
    }


    /**
     * 단일 이미지를 조회하는 메서드입니다
     * 필요하실까봐 만들어놓았습니다!
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    @Override
    public String findImageByFileName(String fileName) {

        MealDiaryImage image = mealDiaryImageRepository.findByFileName(fileName).orElseThrow(
                () -> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));

        return image.getImageLink();
    }


    /**
     * 매핑되어있는 정보를 통해 이미지를 찾는 메서드입니다
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    @Override
    public List<String> findImagesByMealId(Long mealDiaryId) {

        MealDiary mealDiary = findMealDiary(mealDiaryId);

        return mealDiary.getMealDiaryImages().stream()
                .map(MealDiaryImage::getImageLink)
                .collect(Collectors.toList());
    }

    /**
     * 이미지를 업로드하는 메서드입니다
     *
     * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
     * */
    private MealDiaryImage uploadImage(String dirName, MultipartFile file, MealDiary mealDiary) {

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
            throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
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
                .mealDiary(mealDiary)
                .build();
    }




    private MealDiary findMealDiary(Long mealDiaryId) {
        return mealDiaryRepository.findById(mealDiaryId).orElseThrow(
                () -> new MealDiaryHandler(ErrorStatus.MEALDIARY_NOT_FOUND));
    }
}


