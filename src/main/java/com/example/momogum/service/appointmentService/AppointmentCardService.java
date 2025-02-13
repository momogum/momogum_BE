package com.example.momogum.service.appointmentService;

import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.utils.S3UrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.momogum.web.dto.appointment.AppointmentCardDTO.*;

@Service
@RequiredArgsConstructor
public class AppointmentCardService {

    private final S3UrlProvider s3UrlProvider;

    public List<AppointmentCardResponseDTO> getAllCards() {
        return s3UrlProvider.getAllUrls().stream()
                .map(url -> {
                    String category = extractCategoryFromUrl(url);
                    return new AppointmentCardResponseDTO(category, url);
                })
                .collect(Collectors.toList());
    }

    //Enum 형식
    public List<AppointmentCardResponseDTO> getCards(CardCategory category) {
        return getCardsByCategory(category.getCategory());
    }

    //파라미터 형식
    public List<AppointmentCardResponseDTO> getCardsByCategory(String category) {
        List<String> imageUrls = s3UrlProvider.getUrlsByCategory(category);

        return imageUrls.stream()
                .map(url -> new AppointmentCardResponseDTO(category, url))
                .collect(Collectors.toList());
    }

    private String extractCategoryFromUrl(String url) {
        return Arrays.stream(CardCategory.values())
                .map(CardCategory::getCategory)
                .filter(url::contains)
                .findFirst()
                .orElse("unknown");
    }
}
