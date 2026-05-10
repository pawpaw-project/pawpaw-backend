package com.pawpaw.pawpaw.domain.pet.service;

import com.pawpaw.pawpaw.domain.pet.dto.PetRequestDto;
import com.pawpaw.pawpaw.domain.pet.dto.PetResponseDto;
import com.pawpaw.pawpaw.domain.pet.entity.Pet;
import com.pawpaw.pawpaw.domain.pet.repository.PetRepository;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public PetResponseDto createPet(PetRequestDto dto, User user) {
        Pet pet = Pet.builder()
                .user(user)
                .name(dto.getName())
                .species(dto.getSpecies())
                .breed(dto.getBreed())
                .age(dto.getAge())
                .gender(dto.getGender())
                .weight(dto.getWeight())
                .photoUrl(dto.getPhotoUrl())
                .description(dto.getDescription())
                .isNeutered(dto.getIsNeutered())
                .build();

        return new PetResponseDto(petRepository.save(pet));
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getMyPets(User user) {
        return petRepository.findByUserId(user.getId())
                .stream()
                .map(PetResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePet(Long petId, User user) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("펫을 찾을 수 없습니다."));

        if (!pet.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        petRepository.delete(pet);
    }
}
