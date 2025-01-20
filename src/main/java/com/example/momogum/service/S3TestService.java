package com.example.momogum.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class S3TestService {

    @PersistenceContext
    private EntityManager entityManager;


    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;



    @Override
    public String upload(MultipartFile multipartFile, String dirName, String username) throws IOException {

        Member memberByUsername = getMemberByUsername(username);

        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");
        String fileName = dirName + "/" + uniqueFileName;
        File uploadFile = convert(multipartFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        // Save metadata to the database
        saveFileMetadata(originalFileName, fileName, multipartFile.getSize(), multipartFile.getContentType(),memberByUsername, uploadImageUrl);

        return uploadImageUrl;
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
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw new ImageCategoryHandler(ErrorStatus.IMAGE_CONVERT_FAIL);
            }
            return convertFile;
        }
        throw new ImageCategoryHandler(ErrorStatus.IMAGE_CONVERT_FAIL);
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
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
            throw new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_DELETED);
        }
    }


    @Override
    public void saveFileMetadata(String originalFileName, String uniqueFileName, long fileSize, String contentType,
                                 Member findMember, String uploadImageUrl) {

        String name = findMember.getName();


        MemberProfileImage image = MemberProfileImage.builder()
                .imageLink(uploadImageUrl)
                .fileName(uniqueFileName)
                .imageName(originalFileName)
                .member(findMember)
                .build();

        memberProfileImageRepository.save(image);
    }


    @Override
    public byte[] download(String username) throws IOException {


        Member memberByUsername = getMemberByUsername(username);

        MemberProfileImage findMemberProfileImage = memberByUsername.getMemberProfileImages().stream()
                .findFirst()
                .orElseThrow(()->new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_FOUND));


        MemberProfileImage image = memberProfileImageRepository.findById(findMemberProfileImage.getId())
                .orElseThrow(()->new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_FOUND));


        String uniqueFileName = image.getFileName();  // Use full path as S3 key
        log.info("Downloading file from S3 with key: {}", uniqueFileName);

        try {
            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, uniqueFileName));
            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                return inputStream.readAllBytes();
            }
        } catch (AmazonS3Exception e) {
            log.error("S3에서 파일 다운로드 오류: {}", e.getMessage());
            throw new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_ALLOWED);
        }
    }



    // memberId로 이미지 조회
    @Override
    public byte[] downloadImage(Long memberId) throws IOException {


        Member memberById = getMemberById(memberId);

        MemberProfileImage findMemberProfileImage = memberById.getMemberProfileImages().stream()
                .findFirst()
                .orElseThrow(()->new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_FOUND));


        MemberProfileImage image = memberProfileImageRepository.findById(findMemberProfileImage.getId())
                .orElseThrow(()->new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_FOUND));


        String uniqueFileName = image.getFileName();  // Use full path as S3 key
        log.info("Downloading file from S3 with key: {}", uniqueFileName);

        try {
            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, uniqueFileName));
            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                return inputStream.readAllBytes();
            }
        } catch (AmazonS3Exception e) {
            log.error("S3에서 파일 다운로드 오류: {}", e.getMessage());
            throw new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_ALLOWED);
        }
    }






    @Transactional
    @Override
    public void deleteFile(String username) throws FileNotFoundException {
        try {
            // 유저 찾기
            Member memberByUsername = getMemberByUsername(username);

            // 첫 번째 프로필 이미지 찾기
            MemberProfileImage findMemberProfileImage = memberByUsername.getMemberProfileImages().stream()
                    .findFirst()
                    .orElseThrow(()->new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_FOUND));

            // 이미지 ID로 조회
            Long memberProfileImageId = findMemberProfileImage.getId();
            MemberProfileImage image = memberProfileImageRepository.findById(memberProfileImageId)
                    .orElseThrow(()->new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_FOUND));

            // 파일 경로(S3 키)
            String uniqueFileName = image.getFileName();
            if (uniqueFileName != null) {
                // AWS S3에서 파일 삭제
                amazonS3.deleteObject(bucket, uniqueFileName);

                memberByUsername.getMemberProfileImages().remove(image);
                image.setMember(null);

                log.info("Attempting to delete UserProfileImage with ID: {}", memberProfileImageId);

                // 로컬 DB에서 엔티티 삭제
                memberProfileImageRepository.deleteById(memberProfileImageId);

                // 명시적으로 flush 호출
                entityManager.flush();
                // 1차 캐시 클리어
                entityManager.clear();
                log.info("Successfully deleted UserProfileImage with ID: {}", memberProfileImageId);
            } else {
                log.error("파일을 찾을 수 없습니다: ID {}", memberProfileImageId);
            }

        } catch (Exception e) {
            log.error("파일 삭제 중 오류가 발생했습니다: ", e);
            throw new ImageCategoryHandler(ErrorStatus.IMAGE_NOT_DELETED);
        }
    }


    @Override
    public String updateProfileImage(MultipartFile newImageFile, String dirName, String username) throws IOException {
        // 유저 찾기
        Member memberByUsername = getMemberByUsername(username);

        // 기존 프로필 이미지 삭제
        deleteFile(username);

        // 새로운 이미지 업로드
        String originalFileName = newImageFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");
        String fileName = dirName + "/" + uniqueFileName;
        File uploadFile = convert(newImageFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        // 새로운 메타데이터 저장
        saveFileMetadata(originalFileName, fileName, newImageFile.getSize(), newImageFile.getContentType(), memberByUsername, uploadImageUrl);

        return uploadImageUrl;
    }

}
