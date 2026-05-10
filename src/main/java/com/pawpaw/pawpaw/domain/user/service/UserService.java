package com.pawpaw.pawpaw.domain.user.service;

import com.pawpaw.pawpaw.domain.user.dto.UserProfileResponseDto;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Transactional(readOnly = true)
    public UserProfileResponseDto getMyProfile(User user) {
        return new UserProfileResponseDto(user);
    }
}
