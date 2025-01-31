package com.example.momogum.service;

import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    /**
     * 닉네임 중복 확인 메서드
     * @param nickname 사용자 입력 닉네임
     * @return 중복 여부 (true: 중복됨, false: 사용 가능)
     */
    public boolean isNicknameDuplicate(String nickname) {
        boolean exists = userEntityRepository.existsByNickname(nickname);
        log.info("닉네임 '{}' 중복 여부: {}", nickname, exists);
        return exists;
    }
}
