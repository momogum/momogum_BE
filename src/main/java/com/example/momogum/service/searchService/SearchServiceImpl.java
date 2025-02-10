package com.example.momogum.service.searchService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.SearchHandler;
import com.example.momogum.converter.searchConverter.SearchConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.search.FollowStatusDTO;
import com.example.momogum.web.dto.search.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserEntityRepository userEntityRepository;
    private final MealDiaryRepository mealDiaryRepository;
    private final FollowerRepository followerRepository;

    @Override
    public List<SearchDTO.AccountSearchResponseDTO> getAccountSearch(String request, Long currentUserId) {

        validateSearchRequest(request);

        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";


        List<UserEntity> users = userEntityRepository.searchByKeyword(request, requestWithoutSpaces, partialRequest);
        if (users.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        List<Long> searchResultUserIds = users.stream()
                .map(UserEntity::getId)
                .collect(Collectors.toList());


        List<Long> myFollowingUserIds = followerRepository.findFollowingIdsByUserId(currentUserId);

        List<FollowStatusDTO> commonFollowers = followerRepository.findCommonFollowers(searchResultUserIds, myFollowingUserIds);

        Map<Long, List<String>> commonFollowMap = commonFollowers.stream()
                .collect(Collectors.groupingBy(
                        FollowStatusDTO::getUserId,
                        Collectors.mapping(FollowStatusDTO::getFollowerName, Collectors.toList())
                ));

        return users.stream()
                .map(user -> {
                    List<String> commonFollowNames = commonFollowMap.getOrDefault(user.getId(), Collections.emptyList());
                    return SearchConverter.toAccountSearchResponseDTO(
                            user,
                            commonFollowNames,
                            commonFollowNames.size()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchDTO.PostSearchResponseDTO> getPostSearch(String request) {

        validateSearchRequest(request);

        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";


        // 검색 및 슬라이스 반환
        Slice<MealDiary> mealDiaries = mealDiaryRepository.searchByKeyword(
                request, requestWithoutSpaces, partialRequest
        );

        if (mealDiaries.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        return mealDiaries.getContent().stream()
                .map(SearchConverter::toPostSearchResponseDTO)
                .collect(Collectors.toList());
    }

    // 검색어 확인
    private void validateSearchRequest(String request) {
        if (request == null || request.isBlank()) {
            throw new SearchHandler(ErrorStatus.KEYWORD_BLANK);
        }
    }
}
