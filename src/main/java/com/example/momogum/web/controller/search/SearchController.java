package com.example.momogum.web.controller.search;


import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.searchService.SearchService;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.search.SearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Tag(name = "검색 API")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "계정 검색 API")
    @GetMapping("/account")
    public ApiResponse<List<SearchDTO.AccountSearchResponseDTO>> getAccountSearch(
            @RequestParam String request,
            @RequestParam Long userId) {

        List<SearchDTO.AccountSearchResponseDTO> result = searchService.getAccountSearch(request, userId);

        return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "밥일기 검색 API")
    @GetMapping("/mealdiary")
    public ApiResponse<List<SearchDTO.PostSearchResponseDTO>> getPostSearch(
            @RequestParam String request) {

        List<SearchDTO.PostSearchResponseDTO> result = searchService.getPostSearch(request);

        return ApiResponse.onSuccess(result);
    }
}
