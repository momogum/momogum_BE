package com.example.momogum.service.appointment;

import com.example.momogum.domain.utils.S3UrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;

@Service
@RequiredArgsConstructor
public class AppointmentCardService {

    private final S3UrlProvider s3UrlProvider;

    /**
     * 주어진 카테고리 이름에 따라 카드 리스트를 반환합니다.
     * @param category 카테고리 이름 (ex. 'basic', 'fun')
     * @return AppointmentCardResponseDTO 리스트
     */
    public List<AppointmentCardResponseDTO> getCardsByCategory(String category) {
        List<String> imageUrls = s3UrlProvider.getUrlsByCategory(category);

        return imageUrls.stream()
                .map(url -> new AppointmentCardResponseDTO(category,url))
                .collect(Collectors.toList());
    }

    /**
     * 기본 카드 리스트 반환
     */
    public List<AppointmentCardResponseDTO> getBasicCards() {
        return getCardsByCategory("basic");
    }

    /**
     * 재미 카드 리스트 반환
     */
    public List<AppointmentCardResponseDTO> getFunCards() {
        return getCardsByCategory("fun");
    }

}
