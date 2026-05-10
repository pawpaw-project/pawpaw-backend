package com.pawpaw.pawpaw.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class KakaoLoginRequestDto {

    @NotBlank
    private String code;

    @NotBlank
    private String redirectUri;
}
