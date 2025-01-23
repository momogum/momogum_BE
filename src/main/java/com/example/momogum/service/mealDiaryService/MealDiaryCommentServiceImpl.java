package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryComments;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MealDiaryCommentServiceImpl implements MealDiaryCommentService {

    private final UserEntityRepository userEntityRepository;
    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryCommentsRepository mealDiaryCommentsRepository;


    @Override
    public MealDiaryCommentDTO.MealDiaryCommentResponseDTO create(MealDiaryCommentDTO.MealDiaryCommentRequestDTO request){

        MealDiary mealDiary = findMealDiary(request.getMealDiaryId());
        UserEntity user = findUser(request.getUserId());

        MealDiaryComments newComment = MealDiaryComments.builder()
                .content(request.getComment())
                .mealDiary(mealDiary)
                .user(user)
                .build();

        MealDiaryComments saveComment = mealDiaryCommentsRepository.save(newComment);

        return MealDiaryCommentDTO.MealDiaryCommentResponseDTO.builder()
                .mealDiaryCommentId(saveComment.getId())
                .build();
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
