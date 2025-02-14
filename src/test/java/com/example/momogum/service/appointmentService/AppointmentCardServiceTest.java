package com.example.momogum.service.appointmentService;

import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.domain.utils.S3UrlProvider;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class AppointmentCardServiceTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Mock
    private S3UrlProvider s3UrlProvider;

    @InjectMocks
    private AppointmentCardService appointmentCardService;

    @Test
    @DisplayName("S3에서 전체 카드 리스트 조회")
    void testGetAllCards() {
        // given
        when(s3UrlProvider.getAllUrls()).thenReturn(
                List.of(
                        "https://example-bucket.s3.amazonaws.com/basic/image1.jpg",
                        "https://example-bucket.s3.amazonaws.com/fun/image2.jpg"
                )
        );

        // when
        List<AppointmentCardResponseDTO> allCards = appointmentCardService.getAllCards();

        // then
        assertEquals(2, allCards.size());
        assertEquals("basic", allCards.get(0).getCategory());
        assertEquals("fun", allCards.get(1).getCategory());
    }

    @Test
    @DisplayName("S3에서 basic 리스트 조회")
    void testGetCardsByCategory_Basic() {
        // given
        when(s3UrlProvider.getUrlsByCategory("basic")).thenReturn(
                List.of(
                        "https://example-bucket.s3.amazonaws.com/basic/image1.jpg",
                        "https://example-bucket.s3.amazonaws.com/basic/image2.jpg"
                )
        );

        // when
        List<AppointmentCardResponseDTO> basicCards = appointmentCardService.getCardsByCategory("basic");

        // then
        assertEquals(2, basicCards.size());
        assertEquals("basic", basicCards.get(0).getCategory());
        assertEquals("https://example-bucket.s3.amazonaws.com/basic/image1.jpg", basicCards.get(0).getImageUrl());
    }

    @Test
    @DisplayName("S3에서 fun 리스트 조회")
    void testGetCardsByCategory_Fun() {
        // given
        when(s3UrlProvider.getUrlsByCategory("fun")).thenReturn(
                List.of(
                        "https://example-bucket.s3.amazonaws.com/fun/image1.jpg",
                        "https://example-bucket.s3.amazonaws.com/fun/image2.jpg"
                )
        );

        // when
        List<AppointmentCardResponseDTO> funCards = appointmentCardService.getCardsByCategory("fun");

        // then
        assertEquals(2, funCards.size());
        assertEquals("fun", funCards.get(0).getCategory());
        assertEquals("https://example-bucket.s3.amazonaws.com/fun/image1.jpg", funCards.get(0).getImageUrl());
    }

    @Test
    @DisplayName("S3에서 event 리스트 조회")
    void testGetCardsByCategory_Event() {
        // given
        when(s3UrlProvider.getUrlsByCategory("event")).thenReturn(
                List.of(
                        "https://example-bucket.s3.amazonaws.com/event/image1.jpg",
                        "https://example-bucket.s3.amazonaws.com/event/image2.jpg"
                )
        );

        // when
        List<AppointmentCardResponseDTO> eventCards = appointmentCardService.getCardsByCategory("event");

        // then
        assertEquals(2, eventCards.size());
        assertEquals("event", eventCards.get(0).getCategory());
        assertEquals("https://example-bucket.s3.amazonaws.com/event/image1.jpg", eventCards.get(0).getImageUrl());
    }
}
