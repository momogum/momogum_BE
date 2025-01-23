package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryConverter;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryKeywordConverter;
import com.example.momogum.domain.*;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.KeywordRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryKeywordRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryLikesRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MealDiaryServiceImpl implements MealDiaryService {

    private final MealDiaryRepository mealDiaryRepository;
    private final UserEntityRepository userEntityRepository;
    private final MealDiaryKeywordRepository mealDiaryKeywordRepository;
    private final KeywordRepository keywordRepository;

    private final MealDiaryImageUtil mealDiaryImageUtil;

    private final MealDiaryLikesRepository mealDiaryLikesRepository;
    private final MealDiaryBookmarkRepository mealDiaryBookmarkRepository;

    private final MealDiaryCommentsRepository mealDiaryCommentsRepository;

    @Override
    public MealDairiesDTO.CreateStoryResponseDTO save(MealDairiesDTO.CreateStoryRequestDTO request,List<MultipartFile> files) {

        UserEntity byId = findUser(request.getMemberId());
        MealDiary mealDiary = MealDiaryConverter.toMealDiary(request,byId);

        MealDiary newMealDiary = mealDiaryRepository.save(mealDiary);

        String dirName = "meal_diary_images";
        mealDiaryImageUtil.uploadImages(files,dirName,newMealDiary.getId());
        extractedKeyword(request, newMealDiary);


        return MealDiaryConverter.toCreateStoryResponseDTO(newMealDiary);
    }

    @Override
    public MealDairiesDTO.GetMealDiaryResponseDTO get(Long mealDiaryId, Long userId){

        List<String> mealDiaryImages = mealDiaryImageUtil.findImagesByMealId(mealDiaryId);
        MealDiary mealDiary = findMealDiary(mealDiaryId);
        List<String> list = getKeywords(mealDiary);

        UserEntity user = findUser(userId);

        boolean diaryLikeStatus = mealDiaryLikesRepository.existsByUserEntityAndMealDiary(user, mealDiary);
        boolean diaryBookmarkStatus = mealDiaryBookmarkRepository.existsByUserEntityAndMealDiary(user, mealDiary);

        List<MealDiaryComments> byMealDiaryId = mealDiaryCommentsRepository.findByMealDiaryId(mealDiaryId);

        List<MealDiaryCommentReadDTO.MealDiaryReadResponseDTO> comments = byMealDiaryId.stream()
                .map(comment -> MealDiaryCommentReadDTO.MealDiaryReadResponseDTO.builder()
                        .userProfileImagePath(comment.getUser().getProfileImage())
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .build())
                .toList();

        return MealDiaryConverter.toGetMealDiaryResponseDTO(mealDiary,list,mealDiaryImages, diaryLikeStatus,diaryBookmarkStatus,comments);
    }

    @Override
    public List<MealDairiesDTO.GetAllMealDiaryResponseDTO> getAll(Long userId) {
        UserEntity user = findUser(userId);

        return mealDiaryRepository.findByUserEntity(user).stream()
                .map(mealDiary -> {
                    MealDiaryImage image = mealDiary.getMealDiaryImages().stream().findFirst()
                            .orElseThrow(() -> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));
                    return MealDiaryConverter.toGetAllMealDiaryResponseDTO(image);
                })
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Long userId, Long mealDiaryId) throws FileNotFoundException {

        MealDiary mealDiary = findMealDiary(mealDiaryId);
        valid(userId,mealDiary);

        mealDiaryImageUtil.deleteImage(mealDiaryId);
        mealDiaryRepository.delete(mealDiary);
    }







    @Transactional
    protected void valid(Long userId, MealDiary mealDiary) {

        UserEntity user = findUser(userId);

        if (!user.getId().equals(mealDiary.getUserEntity().getId())) {
            throw new UserEntityHandler(ErrorStatus.MEMBER_AUTHENTICATE_FAILED);
        }
    }


    // 밥일기 매핑 키워드 조회 메서드
    private static List<String> getKeywords(MealDiary mealDiary) {
        List<MealDiaryKeyword> mealDiaryKeywords = mealDiary.getMealDiaryKeywords();

        List<Keyword> keywordsEntities = mealDiaryKeywords.stream()
                .map(MealDiaryKeyword::getKeyword)
                .toList();

        return keywordsEntities.stream()
                .map(Keyword::getKeyword)
                .toList();
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
