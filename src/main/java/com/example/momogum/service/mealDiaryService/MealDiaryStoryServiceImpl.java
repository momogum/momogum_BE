package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryStoryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryStoryConverter;
import com.example.momogum.domain.*;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MealDiaryStoryServiceImpl implements MealDiaryStoryService {

    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryStoryRepository mealDiaryStoryRepository;
    private final UserEntityRepository userEntityRepository;

    private final FollowingRepository followingRepository;

    @Override
    public MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO get(Long storyId){

        MealDiaryStory mealDiaryStory = findMealDiaryStory(storyId);
        List<String> imageLinks = mealDiaryStory.getMealDiary().getMealDiaryImages().stream()
                .map(MealDiaryImage::getImageLink)
                .toList();

        return MealDiaryStoryConverter.toMealDiaryStoryReadDTO(mealDiaryStory,imageLinks);
    }


    @Override
    public List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> getAll(Long userId){

        List<UserEntity> followedUsersByUserId = followingRepository.findFollowedUsersByUserId(userId);
        List<MealDiary> findMealDiaries = mealDiaryRepository.findByUserEntityIn(followedUsersByUserId);

        List<MealDiaryStory> byMealDiaryIn = mealDiaryStoryRepository.findByMealDiaryIn(findMealDiaries);

        return MealDiaryStoryConverter.toMealDiaryStoryReadAllDTO(byMealDiaryIn);
    }





    private MealDiaryStory findMealDiaryStory(Long storyId) {
        return mealDiaryStoryRepository.findById(storyId)
                .orElseThrow(() -> new MealDiaryStoryHandler(ErrorStatus.MEALDIARY_STORY_NOT_FOUND));
    }
}
