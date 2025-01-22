package com.example.momogum.service.mealDiaryService;

import com.example.momogum.converter.MealDiaryConverter;
import com.example.momogum.converter.MealDiaryKeywordConverter;
import com.example.momogum.domain.Keyword;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryKeyword;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryRepo.KeywordRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryKeywordRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.MealDairiesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealDiaryServiceImpl implements MealDiaryService {

    private final MealDiaryRepository mealDiaryRepository;
    private final UserEntityRepository userEntityRepository;
    private final MealDiaryKeywordRepository mealDiaryKeywordRepository;
    private final KeywordRepository keywordRepository;

    @Override
    public MealDairiesDTO.CreateStoryResponseDTO save(MealDairiesDTO.CreateStoryRequestDTO request) {

        UserEntity byId = findUser(request);
        MealDiary newMealDiary = MealDiaryConverter.toMealDiary(request,byId);

        mealDiaryRepository.save(newMealDiary);

        extractedKeyword(request, newMealDiary);
        return MealDiaryConverter.toCreateStoryResponseDTO(newMealDiary);

    }













    // 키워드 추출 메서드
    private void extractedKeyword(MealDairiesDTO.CreateStoryRequestDTO request, MealDiary newMealDiary) {

        String[] keywords = request.getKeyword().split(","); // 쉼표로 분리

        if (keywords.length > 5) {
            throw new IllegalArgumentException("키워드는 최대 5개까지만 입력 가능합니다");
        }
        for (String keywordName : keywords) {
            String trimmedKeyword = keywordName.trim();

            // 4-1. 기존에 존재하는 키워드인지 확인
            Keyword keyword = keywordRepository.findByKeyword(trimmedKeyword)
                    .orElseGet(() -> keywordRepository.save(Keyword.builder()
                            .keyword(trimmedKeyword)
                            .build()));

            // 4-2. 매핑 테이블에 저장
            MealDiaryKeyword mealDiaryKeyword = MealDiaryKeywordConverter.toMealDiaryKeyword(newMealDiary,keyword);

            mealDiaryKeywordRepository.save(mealDiaryKeyword);
        }
    }


    // 회원 검색 메서드
    private UserEntity findUser(MealDairiesDTO.CreateStoryRequestDTO request) {
        UserEntity byId = userEntityRepository.findById(request.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("회원을 찾을 수 없습니다"));
        return byId;
    }
}
