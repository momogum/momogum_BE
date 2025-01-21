package com.example.momogum.service.viewMealDiaryService;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryImageRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.redisRepository.RedisRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViewMealDiaryServiceImpl implements ViewMealDiaryService {

    private final RedisRepository redisRepository;
    private final MealDiaryRepository mealDiaryRepository;
    private static final int SIZE = 6;
    private static final int TTL_MINUTES = 5;

    /**
     * 또 올래요 조회 로직
     */
    @Override
    public ViewMealDiaryDTO.ViewMealDiaryResponseListDTO getMealDiaryIsRevisitedByLikesCount(Long userId) {
        // "또 올래요" 표시된 밥일기 ID 리스트 가져오기 (좋아요 개수 순)
        List<Long> isRevisitMealDiaryIds = mealDiaryRepository.findAllByIsRevisit("REVISIT");

        // 공통 로직 호출
        List<ViewMealDiaryDTO.ViewMealDiaryResponse> responseList =
                getUnViewedMealDiaries(userId, isRevisitMealDiaryIds);

        // 결과 반환
        return new ViewMealDiaryDTO.ViewMealDiaryResponseListDTO(responseList);
    }

    /**
     * 음식 카테고리에 따른 조회 로직
     */
    @Override
    public ViewMealDiaryDTO.ViewMealDiaryResponseListDTO getMealDiaryByFoodCategory(Long userId, String foodCategory) {
        // 1. 특정 카테고리의 밥일기 ID 리스트 가져오기 (좋아요 개수 순)
        List<Long> mealDiaryIdsByFoodCategory = mealDiaryRepository.findAllByFoodCategory(foodCategory);

        // 2. 공통 로직 호출
        List<ViewMealDiaryDTO.ViewMealDiaryResponse> responseList =
                getUnViewedMealDiaries(userId, mealDiaryIdsByFoodCategory);

        // 3. 결과 반환
        return new ViewMealDiaryDTO.ViewMealDiaryResponseListDTO(responseList);
    }

    /**
     * 공통 로직 ( 특정 조건에 맞는 mealdiary id 값들의 리스트에서 redis(이미 조회)에 있는 id값들을 빼는 로직
     */
    private List<ViewMealDiaryDTO.ViewMealDiaryResponse> getUnViewedMealDiaries(
            Long userId,
            List<Long> allMealDiaryIds) {

        // redis에서 이미 본 mealdiary id 가져오기
        String redisKey = userId.toString();
        Set<Long> viewedPosts = redisRepository.getViewedPosts(redisKey);

        // redis와 비교하여 중복 제거 + 6개 가져오기
        List<Long> unViewedPostIds = allMealDiaryIds.stream()
                .filter(id -> !viewedPosts.contains(id))
                .limit(ViewMealDiaryServiceImpl.SIZE)
                .toList();

        // 필터링된 Id로 MealDiary 엔티티 조회
        List<MealDiary> unViewedPosts = mealDiaryRepository.findByIdIn(unViewedPostIds);

        // MealDiary 엔티티를 DTO로 변환
        List<ViewMealDiaryDTO.ViewMealDiaryResponse> responseList = unViewedPosts.stream()
                .map(post -> ViewMealDiaryDTO.ViewMealDiaryResponse.builder()
                        .mealDiaryId(post.getId())
                        .foodImageURLs(post.getMealDiaryImages().stream()
                                .map(MealDiaryImage::getImageLink)
                                .toList())
                        .userImageURL(post.getUser().getProfileImage())
                        .foodCategory(post.getFoodCategory())
                        .keyWord(post.getKeyWord())
                        .isRevisit(post.getIsRevisit())
                        .build())
                .toList();

        // redis에 반환된 Post ID 저장
        responseList.forEach(response ->
                redisRepository.addViewedPost(redisKey, response.getMealDiaryId().toString()));

        // redis TTL 설정
        redisRepository.setSessionTimeout(redisKey, TTL_MINUTES);

        return responseList;
    }
}
