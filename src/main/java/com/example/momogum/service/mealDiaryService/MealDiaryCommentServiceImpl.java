package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryCommentHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryCommentConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryComments;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentCreateDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDeleteDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentUpdateDTO;
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
    public MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO create(MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request){

        MealDiary mealDiary = findMealDiary(request.getMealDiaryId());
        UserEntity user = findUser(request.getUserId());
        MealDiaryComments newComment = MealDiaryCommentConverter.toMealDiaryComments(request.getComment(), mealDiary,user);

        MealDiaryComments saveComment = mealDiaryCommentsRepository.save(newComment);
        mealDiary.increaseCommentCount();

        return MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO.builder()
                .mealDiaryCommentId(saveComment.getId())
                .build();
    }

    @Override
    public MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO update(MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request){

        UserEntity findUser = findUser(request.getUserId());
        MealDiaryComments byId = findComment(request.getMealDiaryCommentId());

        userValid(findUser, byId);
        byId.updateContent(request.getComment());

        MealDiaryComments updateComment = mealDiaryCommentsRepository.save(byId);

        return MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO.builder()
                .mealDiaryCommentId(updateComment.getId())
                .build();
    }

    @Override
    @Transactional
    public void delete(MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request){

        MealDiaryComments comment = findComment(request.getMealDiaryCommentId());

        MealDiary mealDiary = comment.getMealDiary();
        if (mealDiary==null){
            throw new MealDiaryHandler(ErrorStatus.MEALDIARY_NOT_FOUND);
        }
        mealDiary.decreaseCommentCount();

        UserEntity user = findUser(request.getUserId());

        userValid(user, comment);

        mealDiaryCommentsRepository.delete(comment);
    }





    private MealDiaryComments findComment(Long mealDiaryCommentId) {
        return mealDiaryCommentsRepository.findById(mealDiaryCommentId)
                .orElseThrow(() -> new MealDiaryCommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
    }


    @Transactional
    protected void userValid(UserEntity findUser, MealDiaryComments byId) {
        if (!findUser.getId().equals(byId.getUser().getId())){
            throw new UserEntityHandler(ErrorStatus.MEMBER_AUTHENTICATE_FAILED);
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
