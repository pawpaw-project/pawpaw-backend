package com.pawpaw.pawpaw.domain.user.dto;

import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfileResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileImg;
    private final LocalDateTime createdAt;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.createdAt = user.getCreatedAt();
    }
}
