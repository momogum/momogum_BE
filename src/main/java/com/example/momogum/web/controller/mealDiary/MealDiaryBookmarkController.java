package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.service.mealDiaryService.MealDiaryBookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meal-diaries/bookmarks")
@Tag(name = "밥일기 북마크 API")
@RequiredArgsConstructor
public class MealDiaryBookmarkController {

    private final MealDiaryBookmarkService mealDiaryBookmarkService;

    // 북마크 토글

    // 북마크한 게시글 조회하는 API
}
