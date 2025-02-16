package com.example.momogum.service.searchService;

import com.example.momogum.web.dto.search.SearchDTO;

import java.util.List;


public interface SearchService {

    List<SearchDTO.AccountSearchResponseDTO> getAccountSearch(String request, Long currentUserId);

    List<SearchDTO.PostSearchResponseDTO> getPostSearch(String request);

    List<SearchDTO.FollowerSearchResponseDTO> getFollowersSearch(Long currentUserId, String request);

    List<SearchDTO.FollowingSearchResponseDTO> getFollowingsSearch(Long currentUserId, String request);
}
