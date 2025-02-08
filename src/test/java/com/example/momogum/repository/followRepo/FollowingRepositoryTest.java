package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.LoginType;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class FollowingRepositoryTest {

    @Autowired
    FollowingRepository followingRepository;

    @Autowired
    UserEntityRepository userEntityRepository;


    @Test
    @DisplayName("findFollowedUsersByUserId()를 이용하여 팔로우하고 있는 회원을 조회 할 수 있다")
    public void findFollowedUsersByUserId_success(){
        //given
        UserEntity testUser1 = UserEntity.builder()
                .phoneNumber("test")
                .name("test")
                .nickname("test")
                .about("test")
                .provider(LoginType.KAKAO)
                .providerId("test1")
                .followerCount(0)
                .followingCount(0)
                .build();

        UserEntity testUser2 = UserEntity.builder()
                .phoneNumber("test")
                .name("test2")
                .nickname("test2")
                .about("test")
                .provider(LoginType.KAKAO)
                .providerId("test2")
                .followerCount(0)
                .followingCount(0)
                .build();

        userEntityRepository.save(testUser1);
        userEntityRepository.save(testUser2);

        // 1L이 2L을 팔로우 하는 경우
        Following testFollowing = Following.builder()
                .id(1L)
                .user(testUser1)
                .following(testUser2)
                .build();

        //when
        followingRepository.save(testFollowing);
        List<UserEntity> response = followingRepository.findFollowedUsersByUserId(1L);

        //then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getId()).isEqualTo(2L);
        assertThat(response.get(0).getNickname()).isEqualTo("test2");
        assertThat(response.get(0).getName()).isEqualTo("test2");

    }

}