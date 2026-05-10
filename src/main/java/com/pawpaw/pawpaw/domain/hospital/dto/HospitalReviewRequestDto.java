package com.pawpaw.pawpaw.domain.hospital.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HospitalReviewRequestDto {
    @NotNull
    @Min(1) @Max(5)
    private Integer rating;
    private String content;
}
