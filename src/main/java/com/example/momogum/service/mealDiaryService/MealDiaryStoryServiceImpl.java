package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryStoryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryStoryConverter;
import com.example.momogum.domain.FollowEntity;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MealDiaryStoryServiceImpl implements MealDiaryStoryService {

    private final MealDiaryStoryRepository mealDiaryStoryRepository;
    private final UserEntityRepository userEntityRepository;
    private final FollowRepository followRepository;

    @Override
    public MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO get(Long storyId){

        MealDiaryStory mealDiaryStory = findMealDiaryStory(storyId);
        List<String> imageLinks = mealDiaryStory.getMealDiary().getMealDiaryImages().stream()
                .map(MealDiaryImage::getImageLink)
                .toList();

        return MealDiaryStoryConverter.toMealDiaryStoryReadDTO(mealDiaryStory,imageLinks);
    }


    public List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> getAll(Long userId){

        // 팔로우 기능이 구현되어야 구현 할 수 있음 FIXME

        return null;
    }





    private UserEntity findUser(Long userId) {
        return userEntityRepository.findById(userId)
                .orElseThrow(()->new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    private MealDiaryStory findMealDiaryStory(Long storyId) {
        return mealDiaryStoryRepository.findById(storyId)
                .orElseThrow(() -> new MealDiaryStoryHandler(ErrorStatus.MEALDIARY_STORY_NOT_FOUND));
    }
}
