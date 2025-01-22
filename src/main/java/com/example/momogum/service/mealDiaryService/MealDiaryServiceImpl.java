package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.MealDiaryConverter;
import com.example.momogum.converter.MealDiaryKeywordConverter;
import com.example.momogum.domain.*;
import com.example.momogum.repository.mealDiaryRepo.KeywordRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryKeywordRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.MealDairiesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MealDiaryServiceImpl implements MealDiaryService {

    private final MealDiaryRepository mealDiaryRepository;
    private final UserEntityRepository userEntityRepository;
    private final MealDiaryKeywordRepository mealDiaryKeywordRepository;
    private final KeywordRepository keywordRepository;

    private final MealDiaryImageUtil mealDiaryImageService;

    @Override
    public MealDairiesDTO.CreateStoryResponseDTO save(MealDairiesDTO.CreateStoryRequestDTO request,List<MultipartFile> files) {

        UserEntity byId = findUser(request.getMemberId());
        MealDiary mealDiary = MealDiaryConverter.toMealDiary(request,byId);

        MealDiary newMealDiary = mealDiaryRepository.save(mealDiary);

        String dirName = "meal_diary_images";
        mealDiaryImageService.uploadImages(files,dirName,newMealDiary.getId());
        extractedKeyword(request, newMealDiary);


        return MealDiaryConverter.toCreateStoryResponseDTO(newMealDiary);
    }

    @Override
    public MealDairiesDTO.GetMealDiaryResponseDTO get(Long mealDiaryId){

        List<String> mealDiaryImages = mealDiaryImageService.findImagesByMealId(mealDiaryId);
        MealDiary mealDiary = findMealDiary(mealDiaryId);
        List<String> list = getKeywords(mealDiary);

        return MealDiaryConverter.toGetMealDiaryResponseDTO(mealDiary,list,mealDiaryImages);
    }

    @Override
    public List<MealDairiesDTO.GetAllMealDiaryResponseDTO> getAll(Long userId){

        UserEntity user = findUser(userId);
        List<MealDairiesDTO.GetAllMealDiaryResponseDTO> result = new ArrayList<>();

        List<MealDiary> byUserEntity = mealDiaryRepository.findByUserEntity(user);

        byUserEntity.stream()
                .forEach(mealDiary -> {
                    MealDiaryImage image = mealDiary.getMealDiaryImages().stream().findFirst()
                            .orElseThrow(()->new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));

                    result.add(MealDiaryConverter.toGetAllMealDiaryResponseDTO(image));
                });

        return result;
    }

    @Override
    public void delete(Long userId, Long mealDiaryId) throws FileNotFoundException {

        MealDiary findMealDiary = findMealDiary(mealDiaryId);
        authenticateUser(userId,findMealDiary);

        MealDiary mealDiary = findMealDiary(mealDiaryId);

        mealDiaryImageService.deleteImage(mealDiaryId);
        mealDiaryRepository.delete(mealDiary);
    }







    @Transactional
    protected void authenticateUser(Long userId, MealDiary mealDiary) {

        UserEntity user = findUser(userId);

        if (!user.getId().equals(mealDiary.getUserEntity().getId())) {
            throw new UserEntityHandler(ErrorStatus.MEMBER_AUTHENTICATE_FAILED);
        }
    }


    // 밥일기 매핑 키워드 조회 메서드
    private static List<String> getKeywords(MealDiary mealDiary) {
        List<MealDiaryKeyword> mealDiaryKeywords = mealDiary.getMealDiaryKeywords();

        List<Keyword> keywords = mealDiaryKeywords.stream()
                .map(MealDiaryKeyword::getKeyword)
                .toList();

        List<String> list = keywords.stream()
                .map(Keyword::getKeyword)
                .toList();
        return list;
    }


    // 키워드 추출 메서드
    private void extractedKeyword(MealDairiesDTO.CreateStoryRequestDTO request, MealDiary newMealDiary) {

        String[] keywords = request.getKeyword().split(","); // 쉼표로 분리

        if (keywords.length > 5) {
            throw new MealDiaryHandler(ErrorStatus.MEALDIARY_KEYWORD_MAX);
        }
        for (String keywordName : keywords) {
            String trimmedKeyword = keywordName.trim();

            // 4-1. 기존에 존재하는 키워드인지 확인
            Keyword keyword = keywordRepository.findByKeyword(trimmedKeyword)
                    .orElseGet(() -> keywordRepository.save(Keyword.builder()
                            .keyword(trimmedKeyword)
                            .build()));

            // 4-2. 매핑 테이블에 저장
            MealDiaryKeyword mealDiaryKeyword = MealDiaryKeywordConverter.toMealDiaryKeyword(newMealDiary,keyword);

            mealDiaryKeywordRepository.save(mealDiaryKeyword);
        }
    }


    // 회원 검색 메서드
    private UserEntity findUser(Long userId) {
        return userEntityRepository.findById(userId)
                .orElseThrow(()->new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    // 밥일기 검색 메서드
    private MealDiary findMealDiary(Long mealDairyId) {
        return mealDiaryRepository.findById(mealDairyId)
                .orElseThrow(()->new MealDiaryHandler(ErrorStatus.MEALDIARY_NOT_FOUND));
    }
}
