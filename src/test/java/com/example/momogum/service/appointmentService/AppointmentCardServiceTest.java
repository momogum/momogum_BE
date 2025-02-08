package com.example.momogum.service.appointmentService;

import com.example.momogum.domain.utils.S3UrlProvider;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentCardServiceTest {

    @Mock
    private S3UrlProvider s3UrlProvider;

    @InjectMocks
    private AppointmentCardService appointmentCardService;

    @Test
    void testGetBasicCards() {

        when(s3UrlProvider.getUrlsByCategory("basic")).thenReturn(
                List.of(
                        "https://example-bucket.s3.amazonaws.com/basic/image1.jpg",
                        "https://example-bucket.s3.amazonaws.com/basic/image2.jpg"
                )
        );

        List<AppointmentCardResponseDTO> basicCards = appointmentCardService.getBasicCards();

        assertEquals(2, basicCards.size());
        assertEquals("basic", basicCards.get(0).getType());
        assertEquals("https://example-bucket.s3.amazonaws.com/basic/image1.jpg", basicCards.get(0).getImageUrl());
    }

    @Test
    void testGetFunCards() {

        when(s3UrlProvider.getUrlsByCategory("fun")).thenReturn(
                List.of(
                        "https://example-bucket.s3.amazonaws.com/fun/image1.jpg",
                        "https://example-bucket.s3.amazonaws.com/fun/image2.jpg"
                )
        );

        //when
        List<AppointmentCardResponseDTO> funCards = appointmentCardService.getFunCards();

        //then
        assertEquals(2, funCards.size());
        assertEquals("fun", funCards.get(0).getType());
        assertEquals("https://example-bucket.s3.amazonaws.com/fun/image1.jpg", funCards.get(0).getImageUrl()); // URL 검증
    }
}
