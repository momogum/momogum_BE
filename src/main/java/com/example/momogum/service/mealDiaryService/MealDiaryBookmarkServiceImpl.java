package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.MealDiaryBookmarkConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MealDiaryBookmarkServiceImpl implements MealDiaryBookmarkService {

    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryBookmarkRepository mealDiaryBookmarkRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public void toggle(Long userId, Long mealDiaryId) {

        MealDiary findMealDiary = findMealDiary(mealDiaryId);
        UserEntity findUser = findUser(userId);

        MealDiaryBookmark mealDiaryBookmark = mealDiaryBookmarkRepository.findByUserEntityAndMealDiary(findUser, findMealDiary)
                .orElse(null);

        if (mealDiaryBookmark != null) {
            mealDiaryBookmarkRepository.delete(mealDiaryBookmark); // 좋아요 삭제
        } else {
            MealDiaryBookmark newMealDiaryBookmark = MealDiaryBookmarkConverter.toMealDiaryBookmark(findUser,findMealDiary);
            mealDiaryBookmarkRepository.save(newMealDiaryBookmark);
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
