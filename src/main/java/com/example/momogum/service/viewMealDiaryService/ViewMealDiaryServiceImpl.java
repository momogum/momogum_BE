package com.example.momogum.service.viewMealDiaryService;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.redisRepository.RedisRepository;
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

    @Override
    public ViewMealDiaryDTO.ViewMealDiaryResponseListDTO getMealDiaryIsRevisitedByLikesCount(Long userId) {

        // 6개씩 주기
        final int size = 6;

        // 또 올래요 표시한 밥일기 Id List로 가져오기 ( 좋아요 개수 순 )
        List<Long> isRevisitMealDiaryIds = mealDiaryRepository.findAllByIsRevisit("REVISIT");

        // 가져온 post redis에 있는지 확인하기
        Set<Long> viewedPosts = redisRepository.getViewedPosts(userId.toString());

        // redis와 비교하여 이미 본 post에 대해선 제거하고 6개 뽑아내기
        List<Long> unViewedPostIds = isRevisitMealDiaryIds.stream()
                .filter(id -> !viewedPosts.contains(id))
                .limit(size)
                .toList();

        // 필터링된 id를 통해서 MealDiary 만들기
        List<MealDiary> unViewedPosts = mealDiaryRepository.findByIdIn(unViewedPostIds);

        // MealDiary 엔티티를 ViewMealDiaryResponse DTO로 변환
        List<ViewMealDiaryDTO.ViewMealDiaryResponse> responseList = unViewedPosts.stream()
                .map(post -> ViewMealDiaryDTO.ViewMealDiaryResponse.builder()
                        .id(post.getId())
                        .foodCategory(post.getFoodCategory())
                        .keyWord(post.getKeyWord())
                        .isRevisit(post.getIsRevisit())
                        .build())
                .toList();

        // Redis에 반환된 Post ID 추가
        responseList.forEach(response ->
                redisRepository.addViewedPost(userId.toString(), response.getId().toString()));

        redisRepository.setSessionTimeout(userId.toString(), 5); // 5분 TTL 설정

        // ViewMealDiaryResponseListDTO로 반환
        return new ViewMealDiaryDTO.ViewMealDiaryResponseListDTO(responseList);

    }

    @Override
    public ViewMealDiaryDTO.ViewMealDiaryResponseListDTO getMealDiaryByFoodCategory(Long userId, String foodCategory) {



        return null;
    }





}
