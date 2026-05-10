package com.pawpaw.pawpaw.domain.walk.dto;

import com.pawpaw.pawpaw.domain.walk.entity.WalkApplication;
import com.pawpaw.pawpaw.domain.walk.entity.WalkApplicationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WalkApplicationResponseDto {

    private Long id;
    private String nickname;
    private WalkApplicationStatus status;
    private LocalDateTime appliedAt;

    public WalkApplicationResponseDto(WalkApplication walkApplication) {
        this.id = walkApplication.getId();
        this.nickname = walkApplication.getUser().getNickname();
        this.status = walkApplication.getStatus();
        this.appliedAt = walkApplication.getAppliedAt();
    }
}
