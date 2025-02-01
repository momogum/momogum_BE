package com.example.momogum.service.searchService;

import com.example.momogum.web.dto.search.SearchDTO;

import java.util.List;


public interface SearchService {

    List<SearchDTO.AccountSearchResponseDTO> getAccountSearch(String request);

    List<SearchDTO.PostSearchResponseDTO> getPostSearch(String request);
}
