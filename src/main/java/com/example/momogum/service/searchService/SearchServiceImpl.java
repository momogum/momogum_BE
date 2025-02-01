package com.example.momogum.service.searchService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.SearchHandler;
import com.example.momogum.converter.searchConverter.SearchConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.search.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserEntityRepository userEntityRepository;
    private final MealDiaryRepository mealDiaryRepository;

    @Override
    public List<SearchDTO.AccountSearchResponseDTO> getAccountSearch(String request) {

        validateSearchRequest(request);

        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";

        List<UserEntity> users = userEntityRepository.searchByKeyword(request, requestWithoutSpaces, partialRequest);


        return users.stream()
                .map(SearchConverter::toAccountSearchResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchDTO.PostSearchResponseDTO> getPostSearch(String request, int page, int size) {

        validateSearchRequest(request);

        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";

        Pageable pageable = PageRequest.of(page, size);

        // 검색 및 슬라이스 반환
        Slice<MealDiary> mealDiaries = mealDiaryRepository.searchByKeyword(
                request, requestWithoutSpaces, partialRequest, pageable
        );

        return mealDiaries.getContent().stream()
                .map(SearchConverter::toPostSearchResponseDTO)
                .collect(Collectors.toList());
    }

    // 검색어 확인
    private void validateSearchRequest(String request) {
        if (request == null || request.isBlank()) {
            throw new SearchHandler(ErrorStatus.KEYWWORD_BLANK);
        }
    }
}
