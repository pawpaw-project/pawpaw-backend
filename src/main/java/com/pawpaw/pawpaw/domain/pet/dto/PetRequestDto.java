package com.pawpaw.pawpaw.domain.pet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PetRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String species;

    private String breed;
    private Integer age;
    private String gender;
    private Double weight;
    private String photoUrl;
    private String description;
    private Boolean isNeutered;
}
