package com.example.momogum.service.searchService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.SearchHandler;
import com.example.momogum.converter.searchConverter.SearchConverter;
import com.example.momogum.domain.*;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.search.FollowStatusDTO;
import com.example.momogum.web.dto.search.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
    private final MealDiaryStoryRepository mealDiaryStoryRepository;


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

        List<MealDiary> findMealDiaries = mealDiaryRepository.findByUserEntityIn(users);
        List<MealDiaryStory> byMealDiaryIn = mealDiaryStoryRepository.findByMealDiaryIn(findMealDiaries);

        return users.stream()
                .map(user -> {
                    List<String> commonFollowNames = commonFollowMap.getOrDefault(user.getId(), Collections.emptyList());
                    return SearchConverter.toAccountSearchResponseDTO(
                            user,
                            commonFollowNames,
                            commonFollowNames.size(),
                            Boolean.TRUE,
                            Boolean.FALSE

                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchDTO.PostSearchResponseDTO> getPostSearch(String request) {

        validateSearchRequest(request);

        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";
        List<String> splitKeywords = Arrays.asList(request.split("\\s+"));


        // 검색 및 슬라이스 반환
        Slice<MealDiary> mealDiaries = mealDiaryRepository.searchByKeyword(
                request, requestWithoutSpaces, partialRequest, splitKeywords
        );

        if (mealDiaries.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        return mealDiaries.getContent().stream()
                .map(mealDiary -> SearchConverter.toPostSearchResponseDTO(mealDiary, request, splitKeywords))
                .collect(Collectors.toList());
    }

    // 검색어 확인
    private void validateSearchRequest(String request) {
        if (request == null || request.isBlank()) {
            throw new SearchHandler(ErrorStatus.KEYWORD_BLANK);
        }
    }

    // 팔로워 목록 중 유저 검색 기능
    @Override
    public List<SearchDTO.FollowerSearchResponseDTO> getFollowersSearch(Long currentUserId, String request) {
        validateSearchRequest(request);
        // 현재 유저를 팔로우하는 사람들의 ID 리스트 가져오기
        List<Long> myFollowerUserIds = followerRepository.findFollowerIdsByUserId(currentUserId);

        if (myFollowerUserIds.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        // 검색 대상 유저 찾기 (내 팔로워 목록 내에서만)
        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";

        List<UserEntity> matchedUsers = userEntityRepository.searchByKeywordAndUserIds(request, myFollowerUserIds);

        if (matchedUsers.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        // DTO 변환 및 반환
        return matchedUsers.stream()
            .map(SearchConverter::toFollowerSearchResponseDTO)
            .collect(Collectors.toList());
    }

    // 팔로잉 목록 중 유저 검색 기능
    @Override
    public List<SearchDTO.FollowingSearchResponseDTO> getFollowingsSearch(Long currentUserId, String request) {
        validateSearchRequest(request);

        // 내가 팔로우하는 사람들의 ID 리스트 가져오기
        List<Long> myFollowingUserIds = followerRepository.findFollowingIdsByUserId(currentUserId);

        if (myFollowingUserIds.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        String requestWithoutSpaces = request.replaceAll("\\s+", "");
        String partialRequest = "%" + request + "%";

        List<UserEntity> matchedUsers = userEntityRepository.searchByKeywordAndUserIds(request, myFollowingUserIds);

        if (matchedUsers.isEmpty()) {
            throw new SearchHandler(ErrorStatus.NO_RESULT_FOUND);
        }

        return matchedUsers.stream()
            .map(SearchConverter::toFollowingSearchResponseDTO)
            .collect(Collectors.toList());
    }


}
