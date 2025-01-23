package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryLikeConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryLikes;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryLikesRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryLikeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MealDiaryLikeServiceImpl implements MealDiaryLikeService {

    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryLikesRepository mealDiaryLikesRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public void toggle(Long userId, Long mealDiaryId) {

        MealDiary findMealDiary = findMealDiary(mealDiaryId);
        UserEntity findUser = findUser(userId);

        MealDiaryLikes mealDiaryLikes = mealDiaryLikesRepository.findByUserEntityAndMealDiary(findUser, findMealDiary)
                .orElse(null);

        if (mealDiaryLikes != null) {
            findMealDiary.decreaseLikeCount(); // 좋아요 수 감소
            mealDiaryLikesRepository.delete(mealDiaryLikes); // 좋아요 삭제
        } else {
            MealDiaryLikes newMealDiaryLikes = MealDiaryLikeConverter.toMealDiaryLikes(findUser,findMealDiary);

            findMealDiary.increaseLikeCount();
            mealDiaryLikesRepository.save(newMealDiaryLikes);
        }
    }

    @Override
    public List<MealDiaryLikeDTO.MealDiaryLikeResponseDTO> get(Long mealDiaryId){

        MealDiary findMealDiary = findMealDiary(mealDiaryId);
        List<MealDiaryLikes> byMealDiary = mealDiaryLikesRepository.findByMealDiary(findMealDiary);

        return byMealDiary.stream()
                .map(MealDiaryLikes::getUserEntity)
                .map(MealDiaryLikeConverter::toMealDiaryLikeResponseDTO)
                .collect(Collectors.toList());
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
