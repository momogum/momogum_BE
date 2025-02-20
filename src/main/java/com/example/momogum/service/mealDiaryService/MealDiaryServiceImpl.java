package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryCommentConverter;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryConverter;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryKeywordConverter;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryReportConverter;
import com.example.momogum.domain.*;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.*;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentReadDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryUpdateDTO;
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
    private final MealDiaryCommentsRepository mealDiaryCommentsRepository;

    private final MealDiaryLikesRepository mealDiaryLikesRepository;
    private final MealDiaryBookmarkRepository mealDiaryBookmarkRepository;
    private final MealDiaryReportRepository mealDiaryReportRepository;
    private final MealDiaryStoryRepository mealDiaryStoryRepository;

    @Override
    public MealDairiesDTO.CreateStoryResponseDTO save(Long userId, MealDairiesDTO.CreateStoryRequestDTO request,List<MultipartFile> files) {

        UserEntity byId = findUser(userId);
        MealDiary mealDiary = MealDiaryConverter.toMealDiary(request,byId);

        MealDiary newMealDiary = mealDiaryRepository.save(mealDiary);

        String dirName = "meal_diary_images";
        mealDiaryImageUtil.uploadImages(files,dirName,newMealDiary.getId());
        extractedKeyword(request, newMealDiary);

        MealDiaryStory newMealDiaryStory = MealDiaryStory.builder()
                .name(byId.getNickname())
                .mealDiary(mealDiary)
                .build();

        mealDiaryStoryRepository.save(newMealDiaryStory);

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
                .map(MealDiaryCommentConverter::toMealDiaryCommentReadDTO)
                .toList();

        UserEntity mealDiaryOwnerUser = mealDiary.getUserEntity();

        String mealDiaryOwnerUserImageLink;
        if (mealDiaryOwnerUser.getProfileImage() == null){
            mealDiaryOwnerUserImageLink = "default_image";
        }else {
            mealDiaryOwnerUserImageLink = mealDiaryOwnerUser.getProfileImage().getImageLink();
        }


        return MealDiaryConverter.toGetMealDiaryResponseDTO(mealDiary,list,mealDiaryImages, diaryLikeStatus,diaryBookmarkStatus,comments,mealDiaryOwnerUserImageLink);
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


    @Override
    public MealDiaryReportDTO.MealDiaryReportResponseDTO report(Long userId, MealDiaryReportDTO.MealDiaryReportRequestDTO request){

        UserEntity user = findUser(userId);
        MealDiary mealDiary = findMealDiary(request.getMealDiaryId());

        validMealDiaryExist(user, mealDiary);

        MealDiaryReport newReport = MealDiaryReportConverter.toMealDiaryReport(user,mealDiary,request.getReportReason());

        mealDiaryReportRepository.save(newReport);
        mealDiary.setReport();

        return MealDiaryReportConverter.mealDiaryReportResponseDTO(mealDiary);
    }


    @Override
    public List<MealDiaryReportDTO.MealDiaryReportResponseDTO> getReport(){

        List<MealDiaryReport> allReport = mealDiaryReportRepository.findAll();

        return MealDiaryReportConverter.toMealDiaryReportResponseDTOList(allReport);
    }


    @Override
    public MealDiaryUpdateDTO.MealDiaryUpdateResponseDTO update(Long userId, MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO request){

        UserEntity findUser = findUser(userId);
        MealDiary findMealDiary = findMealDiary(request.getMealDiaryId());

        valid(findUser.getId(),findMealDiary);

        removeMealDiaryKeyword(findMealDiary);
        updateKeyword(request,findMealDiary);

        Long result = findMealDiary.update(request);
        mealDiaryRepository.saveAndFlush(findMealDiary); // 즉시 반영

        return MealDiaryUpdateDTO.MealDiaryUpdateResponseDTO.builder()
                .mealDiaryId(findMealDiary.getId())
                .build();
    }







    @Transactional
    protected void valid(Long userId, MealDiary mealDiary) {

        UserEntity user = findUser(userId);

        if (!user.getId().equals(mealDiary.getUserEntity().getId())) {
            throw new UserEntityHandler(ErrorStatus.MEMBER_AUTHENTICATE_FAILED);
        }
    }

    public void updateKeyword(MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO request, MealDiary newMealDiary) {

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


    public void removeMealDiaryKeyword(MealDiary mealDiary){
        mealDiary.removeMealDiaryKeywordAll();

        mealDiaryKeywordRepository.deleteAllByMealDiary(mealDiary);
    }

    private void validMealDiaryExist(UserEntity user, MealDiary mealDiary) {
        boolean isReport = mealDiaryReportRepository.existsByUserEntityAndMealDiary(user, mealDiary);

        if(isReport){
            throw new MealDiaryHandler(ErrorStatus.MEALDIARY_REPORTED);
        }
    }


    // 밥일기 매핑 키워드 조회 메서드
    public List<String> getKeywords(MealDiary mealDiary) {
        List<MealDiaryKeyword> mealDiaryKeywords = mealDiary.getMealDiaryKeywords();

        List<Keyword> keywordsEntities = mealDiaryKeywords.stream()
                .map(MealDiaryKeyword::getKeyword)
                .toList();

        return keywordsEntities.stream()
                .map(Keyword::getKeyword)
                .toList();
    }


    // 키워드 추출 메서드
    public void extractedKeyword(MealDairiesDTO.CreateStoryRequestDTO request, MealDiary newMealDiary) {

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
