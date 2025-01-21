package com.example.momogum.service.appointment;

import com.example.momogum.domain.utils.S3UrlProvider;
import com.example.momogum.web.dto.appointment.AppointMentDTO.AppointmentCardResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
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
        List<AppointmentCardResponseDTO> funCards = appointmentCardService.getFunCards();
        assertEquals(2, funCards.size());
        assertEquals("basic", funCards.get(0).getType());
        assertEquals("https://example-bucket.s3.amazonaws.com/fun/image1.jpg", funCards.get(0).getImageUrl());
    }


}