package com.example.momogum.service.userProfileService;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowerResponseDTO;
import java.util.List;
import org.springframework.stereotype.Service;


public interface FollowService {

  FollowDTO.FollowStatsDTO toggleFollowUser(Long currentUserId, Long targetUserId);

  List<FollowDTO.FollowingResponseDTO> getFollowings(Long userId);

  List<FollowDTO.FollowerResponseDTO> getFollowers(Long userId);

 // void updateFollowCounts(UserEntity user);

  FollowDTO.FollowStatsDTO getFollowStats(Long userId);

  Boolean isMutualFollow(UserEntity currentUser, UserEntity targetUser);

  List<FollowDTO.FollowingResponseDTO> searchFollowingsByQuery(Long userId, String query);

  List<FollowDTO.FollowerResponseDTO> searchFollowersByQuery(Long userId, String query);

}
