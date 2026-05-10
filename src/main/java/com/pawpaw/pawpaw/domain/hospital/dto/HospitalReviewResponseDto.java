package com.pawpaw.pawpaw.domain.hospital.dto;

import com.pawpaw.pawpaw.domain.hospital.entity.HospitalReview;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class HospitalReviewResponseDto {
    private Long id;
    private String nickname;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;

    public HospitalReviewResponseDto(HospitalReview review) {
        this.id = review.getId();
        this.nickname = review.getUser().getNickname();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
