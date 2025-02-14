package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryStoryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryStoryConverter;
import com.example.momogum.domain.*;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryViewRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MealDiaryStoryServiceImpl implements MealDiaryStoryService {

    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryStoryRepository mealDiaryStoryRepository;
    private final FollowingRepository followingRepository;
    private final UserEntityRepository userEntityRepository;
    private final MealDiaryStoryViewRepository mealDiaryStoryViewRepository;

    @Override
    public MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO get(Long memberId,Long storyId){

        UserEntity findUser = findUser(memberId);

        MealDiaryStory mealDiaryStory = findMealDiaryStory(storyId);
        List<String> imageLinks = mealDiaryStory.getMealDiary().getMealDiaryImages().stream()
                .map(MealDiaryImage::getImageLink)
                .toList();

        MealDiaryStoryView newMealDiaryView = MealDiaryStoryView.builder()
                .mealDiaryStory(mealDiaryStory)
                .isViewed(true)
                .userEntity(findUser)
                .build();
        mealDiaryStoryViewRepository.save(newMealDiaryView);

        return MealDiaryStoryConverter.toMealDiaryStoryReadDTO(mealDiaryStory,imageLinks);
    }


    @Override
    public List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> getAll(Long userId){

        // 회원을 찾고
        UserEntity findUser = findUser(userId);

        // 회원이 팔로우하는 회원이 작성한 스토리를 조회한다
        List<UserEntity> followedUsersByUserId = followingRepository.findFollowedUsersByUserId(userId);
        List<MealDiary> findMealDiaries = mealDiaryRepository.findByUserEntityIn(followedUsersByUserId);
        List<MealDiaryStory> byMealDiaryIn = mealDiaryStoryRepository.findByMealDiaryIn(findMealDiaries);

        return byMealDiaryIn.stream().map(mealDiaryStory -> {
            // 스토리들을 하나씩 조회하며 DTO를 만들고 반환한다
            List<MealDiaryImage> mealDiaryImages = mealDiaryStory.getMealDiary().getMealDiaryImages();
            String name = mealDiaryStory.getName();

            // 이때 회원이 해당 스토리를 조회하였는지 여부를 같이 추가한다 -> Converter를 쓸 수 없는 이유
            // converter가 repository를 의존하게 되기 때문
            MealDiaryStoryView isViewedEntity = mealDiaryStoryViewRepository.findByUserEntityAndMealDiaryStory(findUser, mealDiaryStory);
            boolean isViewed = isViewedEntity != null && isViewedEntity.isViewed();

            String imageLink = (mealDiaryImages != null && !mealDiaryImages.isEmpty()) ? mealDiaryImages.get(0).getImageLink() : null;

            return MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO.builder()
                    .mealDiaryImageLinks(imageLink)
                    .nickname(name)
                    .isViewed(isViewed)
                    .build();
        }).toList();
    }


    @Override
    public MealDiaryStoryReadDTO.MyMealDiaryStoryReadResponseDTO getMine(Long userId){
        UserEntity findUser = findUser(userId);
        List<MealDiary> byUserEntity = mealDiaryRepository.findByUserEntity(findUser);
        List<MealDiaryStory> byMealDiaryIn = mealDiaryStoryRepository.findByMealDiaryIn(byUserEntity);
        MealDiaryStory mealDiaryStory = byMealDiaryIn != null && !byMealDiaryIn.isEmpty() ? byMealDiaryIn.get(0) : null;
        String imageLink = mealDiaryStory != null ? mealDiaryStory.getMealDiary().getMealDiaryImages().get(0).getImageLink() : null;

        return MealDiaryStoryReadDTO.MyMealDiaryStoryReadResponseDTO.builder()
                .nickname(findUser.getNickname())
                .mealDiaryImageLinks(imageLink)
                .build();
    }

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void delete(){
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        // 원활한 테스트를 위해 int를 반환
        int deletedCount = mealDiaryStoryRepository.deleteByCreatedAtBefore(threeDaysAgo);
        log.info("삭제된 스토리 갯수: {}", deletedCount);
    }






    private UserEntity findUser(Long memberId) {
        return userEntityRepository.findById(memberId)
                .orElseThrow(() -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    private MealDiaryStory findMealDiaryStory(Long storyId) {
        return mealDiaryStoryRepository.findById(storyId)
                .orElseThrow(() -> new MealDiaryStoryHandler(ErrorStatus.MEALDIARY_STORY_NOT_FOUND));
    }
}
