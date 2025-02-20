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
import com.example.momogum.util.FirebaseCloudMessageUtil;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentCreateDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDeleteDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional
public class MealDiaryCommentServiceImpl implements MealDiaryCommentService {

    private final UserEntityRepository userEntityRepository;
    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryCommentsRepository mealDiaryCommentsRepository;
    private final FirebaseCloudMessageUtil firebaseCloudMessageUtil;


    @Override
    public MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO create(Long userId, MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request) throws IOException {

        MealDiary mealDiary = findMealDiary(request.getMealDiaryId());
        UserEntity commentOwner = findUser(userId);
        MealDiaryComments newComment = MealDiaryCommentConverter.toMealDiaryComments(request.getComment(), mealDiary,commentOwner);

        MealDiaryComments saveComment = mealDiaryCommentsRepository.save(newComment);
        mealDiary.increaseCommentCount();

        UserEntity mealDiaryOwner = mealDiary.getUserEntity();

        if (mealDiaryOwner.getFcmToken() == null){
            return MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO.builder()
                    .mealDiaryCommentId(saveComment.getId())
                    .build();
        }else {
            String title = mealDiaryOwner.getNickname();
            String body = commentOwner.getName()+"님이 댓글을 작성하였습니다";
            firebaseCloudMessageUtil.sendMessageTo(mealDiaryOwner.getId(),title,body);

            return MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO.builder()
                    .mealDiaryCommentId(saveComment.getId())
                    .build();
        }
    }

    @Override
    public MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO update(Long userId, MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request){

        UserEntity findUser = findUser(userId);
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
    public void delete(Long userId, MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request){

        MealDiaryComments comment = findComment(request.getMealDiaryCommentId());

        MealDiary mealDiary = comment.getMealDiary();
        if (mealDiary==null){
            throw new MealDiaryHandler(ErrorStatus.MEALDIARY_NOT_FOUND);
        }
        mealDiary.decreaseCommentCount();

        UserEntity user = findUser(userId);

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

    public void sendPushAlarm(String commentOwnerNickName, Long mealDiaryOwnerId) throws IOException {
        String title = "댓글이 작성되었습니다";
        String body = commentOwnerNickName +"님이 댓글을 작성하였습니다";
        firebaseCloudMessageUtil.sendMessageTo(mealDiaryOwnerId,title,body);
    }

}
