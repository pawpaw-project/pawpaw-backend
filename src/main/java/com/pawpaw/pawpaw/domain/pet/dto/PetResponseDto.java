package com.pawpaw.pawpaw.domain.pet.dto;

import com.pawpaw.pawpaw.domain.pet.entity.Pet;
import lombok.Getter;

@Getter
public class PetResponseDto {

    private Long id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private Double weight;
    private String photoUrl;
    private String description;
    private Boolean isNeutered;

    public PetResponseDto(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.species = pet.getSpecies();
        this.breed = pet.getBreed();
        this.age = pet.getAge();
        this.gender = pet.getGender();
        this.weight = pet.getWeight();
        this.photoUrl = pet.getPhotoUrl();
        this.description = pet.getDescription();
        this.isNeutered = pet.getIsNeutered();
    }
}
