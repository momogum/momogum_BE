package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TestProfileImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미지가 저장된 주소 링크
    @Column(nullable = false)
    private String imageLink;

    @Column(nullable = false, columnDefinition = "text")
    private String fileName;

    @Column(nullable = false)
    private String imageName;
}
