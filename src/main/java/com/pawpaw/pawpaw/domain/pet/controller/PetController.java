package com.pawpaw.pawpaw.domain.pet.controller;

import com.pawpaw.pawpaw.domain.pet.dto.PetRequestDto;
import com.pawpaw.pawpaw.domain.pet.dto.PetResponseDto;
import com.pawpaw.pawpaw.domain.pet.service.PetService;
import com.pawpaw.pawpaw.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDto> createPet(
            @Valid @RequestBody PetRequestDto dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(petService.createPet(dto, user));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PetResponseDto>> getMyPets(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(petService.getMyPets(user));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long petId,
            @AuthenticationPrincipal User user) {
        petService.deletePet(petId, user);
        return ResponseEntity.ok().build();
    }
}
