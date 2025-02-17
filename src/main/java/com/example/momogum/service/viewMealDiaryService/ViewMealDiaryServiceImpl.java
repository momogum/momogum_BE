package com.example.momogum.service.viewMealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.ViewMealDiaryConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;

import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.redisRepository.RedisRepository;

import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.MainViewMealDiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class ViewMealDiaryServiceImpl implements ViewMealDiaryService {

    private final RedisRepository redisRepository;
    private final MealDiaryRepository mealDiaryRepository;

    private static final int TTL_MINUTES = 1;

    /**
     * 또 올래요 조회 로직
     */
    @Override
    public List<MainViewMealDiaryResponse> getMealDiaryIsRevisitedByLikesCount(Long userId) {

        Pageable pageable = PageRequest.of(0, 6);

        String redisKey = userId.toString();
        Set<Long> viewedPosts = redisRepository.getViewedPosts(redisKey);

        List<Long> isRevisitMealDiaryIds = mealDiaryRepository.findAllByIsRevisit(IsRevisit.GOOD, userId, new ArrayList<>(viewedPosts), pageable);

        return getUnViewedMealDiaries(userId, isRevisitMealDiaryIds);

    }

    /**
     * 음식 카테고리에 따른 조회 로직
     */
    @Override
    public List<MainViewMealDiaryResponse> getMealDiaryByFoodCategory(Long userId, FoodCategory foodCategory) {

        Pageable pageable = PageRequest.of(0, 6);

        String redisKey = userId.toString();
        Set<Long> viewedPosts = redisRepository.getViewedPosts(redisKey);

        List<Long> foodCategoryMealDiaryIds = mealDiaryRepository.findAllByFoodCategory(foodCategory, userId, new ArrayList<>(viewedPosts), pageable);

        return getUnViewedMealDiaries(userId, foodCategoryMealDiaryIds);
    }

    /**
     * 공통 로직 (반환될 mealDiaryId는 redis에 추가 / converter로 dto 변환 후 List 형태로 반환)
     */
    private List<MainViewMealDiaryResponse> getUnViewedMealDiaries(
            Long userId,
            List<Long> mealDiaryIds) {

        if (mealDiaryIds.isEmpty()) {
            return List.of(); // 조회할 데이터가 없는 경우 빈 리스트 반환
        }

        // MealDiary 엔티티 조회
        List<MealDiary> unViewedPosts = mealDiaryRepository.findByIdIn(mealDiaryIds);
        unViewedPosts.sort(Comparator.comparingLong(mealDiary -> mealDiaryIds.indexOf(mealDiary.getId())));

        // MealDiary 엔티티를 DTO로 변환
        List<MainViewMealDiaryResponse> responseList = unViewedPosts.stream()
                .map(ViewMealDiaryConverter::toMainViewMealDiaryResponse)
                .toList();

        // Redis에 반환된 Post ID 저장
        String redisKey = userId.toString();
        responseList.forEach(response ->
                redisRepository.addViewedPost(redisKey, response.getMealDiaryId().toString()));

        // Redis TTL 설정
        redisRepository.setSessionTimeout(redisKey, TTL_MINUTES);

        return responseList;
    }

    // 사용자 관련 예외
    private String getUserProfileImage(MealDiary post) {
        if (post.getUserEntity() == null) {
            throw new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        if (post.getUserEntity().getProfileImage() == null) {
            throw new UserEntityHandler(ErrorStatus.PROFILE_IMAGE_NOT_FOUND);
        }

        return post.getUserEntity().getProfileImage().getImageLink();
    }

    // 키워드 예외
    private List<String> getKeywords(MealDiary post) {
        if (post.getMealDiaryKeywords() == null) {
            return List.of();
        }

        return post.getMealDiaryKeywords().stream()
                .map(mealDiaryKeyword -> mealDiaryKeyword.getKeyword().getKeyword())
                .toList();
    }

    // 음식 사진 예외
    private List<String> getFoodImageURLs(MealDiary post) {
        if (post.getMealDiaryImages() == null || post.getMealDiaryImages().isEmpty()) {
            throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
        }

        return post.getMealDiaryImages().stream()
                .map(MealDiaryImage::getImageLink)
                .toList();
    }
}
