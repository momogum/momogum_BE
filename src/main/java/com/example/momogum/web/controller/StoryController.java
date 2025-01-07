package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.StoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/story")
@Tag(name = "스토리 관련 API")
public class StoryController {



    /**
     * 스토리 추가
     *
     *  1. 사진선택
     *  2. 식사메뉴 선택 ( 한식, 중식, 일식....근데 카테고리가 얼마나 어떻게 있는지 모르겠음 )
     *  3. #키워드 선택 ( 이것도 작동방식과 사용 관련해서 여쭤봐야할듯 )
     *  4. 정확한 메뉴 이름
     *  5. 식사 위치
     *  6. 간단한 식사 경험
     *  7. 또 오고 싶은 곳인가요? ( 밑의 항목들 중에 선택 )
     *      -> 재방문 의사 1번 정도 있어요
     *      -> 같은 메뉴를 또 먹어보고 싶어요
     *      -> 이곳의 다른 메뉴도 궁금해요
     *      -> 자주 오고 싶어요
     *      -> 다시 방문하고 싶지 않아요
     *  8. 전체 평점 ( 0.1점 씩 올라감, 5점만점)
     * */
    @Operation(summary = "스토리 생성 API")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<StoryDTO.CreateResponseDTO> create(
            @Parameter(description = "스토리 생성 시, 사용되는 이미지 파일입니다.")
            @RequestPart(value = "file") MultipartFile multipartFile,
            @Parameter(description = "기술명세서 하단의 스키마를 확인해주세요")
            @RequestBody StoryDTO.CreateRequestDTO createRequestDTO) {

        return ApiResponse.onSuccess(StoryDTO.CreateResponseDTO.builder()
                // API 구현시에는 수정될 예정입니다 FIXME
                .storyId(1L)
                .build());
    }



    /**
     * 스토리 조회 (내가 확인한게 스토리 조회가 맞는지 모르겠음)
     *
     *  1. 자신이 팔로우하고 있는 회원들의 스토리를 조회
     *  2. 최신순으로 조회
     *  3. 조회 여부를 남길 수 있는 필드 필요
     *  4. 기획에서 기억나는 부분으로는 3일동안 조회 가능한걸로 기억함
     *      3일이 지난 후에는 스토리 보관함으로 이동하고
     *      여기서 하이라이트를 만들 수 있음
     *
     *  - 인스타 스토리같이 조회되는 기능이라면 읽지 않은 스토리를 우선적으로 조회하고,
     *      그 다음 조회하지 않은 스토리를 조회해야함
     * */


    /**
     * 개별 스토리 조회
     * */
    @Operation(summary = "개별 스토리 조회 API 입니다")
    @GetMapping("/{storyId}")
    public ApiResponse<StoryDTO.GetResponseDTO> getOne(@PathVariable Long storyId) {

        return ApiResponse.onSuccess(StoryDTO.GetResponseDTO.builder()
                .score(1)
                .foodCategory("temp")
                .keyword("temp")
                .location("temp")
                .review("temp")
                .imagePath("temp")
                .build());
    }


    /**
     * 자신의 보관 스토리 전부 조회
     *
     *  1. 회원정보를 받아서
     *  2. 그걸 가지고 회원에 매핑된 스토리 전부 조회
     *
     * */



    /**
     * 스토리 하이라이트 생성
     *
     *  0. 하이라이트에 넣을 스토리 넣기
     *  1. 커버 이미지 생성
     *  2. 하이라이트 이름
     *
     * */



    /**
     * 보관중인 스토리 하이라이트에 추가 (와이어프레임 상에 없는 기능) -> 토글식으로 구현하는 것도 방법이려나
     *
     *  1. 스토리 식별자를 통해서 하이라이트에 추가
     *
     * */



    // 스토리 수정기능이 필요하려나?



    /**
     * 스토리 삭제
     *
     * 이건 바로 삭제로 할지 며칠간 보관하고 복구가 가능하도록 구현할지
     *
     * 일단 바로 삭제로 생각하고 구현
     * */
}
