package com.pawpaw.pawpaw.domain.user.controller;

import com.pawpaw.pawpaw.domain.user.dto.UserProfileResponseDto;
import com.pawpaw.pawpaw.domain.user.entity.User;
import com.pawpaw.pawpaw.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getMyProfile(user));
    }
}
