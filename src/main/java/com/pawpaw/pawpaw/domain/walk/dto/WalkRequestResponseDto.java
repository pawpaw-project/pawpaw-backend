package com.pawpaw.pawpaw.domain.walk.dto;

import com.pawpaw.pawpaw.domain.walk.entity.WalkRequest;
import com.pawpaw.pawpaw.domain.walk.entity.WalkRequestStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
public class WalkRequestResponseDto {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String nickname;
    private String petName;
    private LocalDate walkDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reward;
    private String location;
    private WalkRequestStatus status;
    private LocalDateTime createdAt;

    public WalkRequestResponseDto(WalkRequest walkRequest) {
        this.id = walkRequest.getId();
        this.userId = walkRequest.getUser().getId();
        this.title = walkRequest.getTitle();
        this.content = walkRequest.getContent();
        this.nickname = walkRequest.getUser().getNickname();
        this.petName = walkRequest.getPet().getName();
        this.walkDate = walkRequest.getWalkDate();
        this.startTime = walkRequest.getStartTime();
        this.endTime = walkRequest.getEndTime();
        this.reward = walkRequest.getReward();
        this.location = walkRequest.getLocation();
        this.status = walkRequest.getStatus();
        this.createdAt = walkRequest.getCreatedAt();
    }
}
