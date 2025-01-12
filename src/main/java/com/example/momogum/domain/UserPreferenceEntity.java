package com.example.momogum.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ElementCollection
    private List<String> favoriteCategories;

    @ElementCollection
    private List<String> favoriteKeywords;

    private String favoriteMenu;

    @ElementCollection
    private List<String> leastFavoriteCategories;

    private LocalDateTime updatedAt;
}
