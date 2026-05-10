package com.pawpaw.pawpaw.domain.walk.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
public class WalkRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long petId;

    @NotNull
    @FutureOrPresent
    private LocalDate walkDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private String reward;
    private String location;
}
