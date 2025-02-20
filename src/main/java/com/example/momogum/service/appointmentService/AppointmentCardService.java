package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.utils.S3UrlProvider;
import com.example.momogum.repository.appoinmentRepo.AppointmentCardRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.momogum.web.dto.appointment.AppointmentCardDTO.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentCardService {

    private final S3UrlProvider s3UrlProvider;
    private final AppointmentCardRepository appointmentCardRepository;
    private final AppointmentRepository appointmentRepository;

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

    /**
     * 카드 선택 (이미 있으면 업데이트, 없으면 새로 저장)
     */
    @Transactional
    public AppointmentCardResponseDTO selectCard(Long appointmentId, AppointmentCardRequestDTO request) {
        // 1. 해당 약속 정보 가져오기
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST));

        // 2. 카드 저장 또는 업데이트
        AppointmentCard selectedCard = saveSelectedCards(appointment, request.getImageUrl(), request.getCategory());

        // 3. 응답 DTO 변환 후 반환
        return new AppointmentCardResponseDTO(selectedCard.getCategory().getCategory(), selectedCard.getImageUrl());
    }

    /**
     * 카드 저장 및 업데이트
     */
    @Transactional
    public AppointmentCard saveSelectedCards(Appointment appointment, String selectedUrl, CardCategory category) {
        Optional<AppointmentCard> findCard = appointmentCardRepository.findByAppointment(appointment);

        //카드가 있을 경우 업데이트
        if (findCard.isPresent()) {
            AppointmentCard card = findCard.get();
            card.setImageUrl(selectedUrl);
            card.setCategory(category);
            return appointmentCardRepository.save(card); // 업데이트된 객체 저장
        }

        //카드가 없을 경우 새로 저장
        AppointmentCard appointmentCard = AppointmentCard.builder()
                .appointment(appointment)
                .imageUrl(selectedUrl)
                .category(category)
                .build();

        return appointmentCardRepository.save(appointmentCard);
    }



    private String extractCategoryFromUrl(String url) {
        return Arrays.stream(CardCategory.values())
                .map(CardCategory::getCategory)
                .filter(url::contains)
                .findFirst()
                .orElse("unknown");
    }
}
