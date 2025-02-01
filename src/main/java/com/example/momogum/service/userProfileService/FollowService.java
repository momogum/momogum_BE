package com.example.momogum.service.userProfileService;

import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowerResponseDTO;
import java.util.List;
import org.springframework.stereotype.Service;


public interface FollowService {

  // 토글 관련 API는 프론트 측과 상의해보고 마저 작성하도록 하겠습니다.
  // void toggleFollowUser(String userId, Long targetId);

  List<FollowDTO.FollowingResponseDTO> getFollowings(Long userId);

  List<FollowDTO.FollowerResponseDTO> getFollowers(Long userId);

  FollowDTO.FollowStatsDTO getFollowStats(Long userId);
}
