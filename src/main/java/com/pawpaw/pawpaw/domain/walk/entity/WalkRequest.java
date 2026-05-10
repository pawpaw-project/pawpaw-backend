package com.pawpaw.pawpaw.domain.walk.entity;

import com.pawpaw.pawpaw.domain.pet.entity.Pet;
import com.pawpaw.pawpaw.domain.user.entity.User;
import com.pawpaw.pawpaw.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "walk_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class WalkRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate walkDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String reward;
    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WalkRequestStatus status;

    public void updateStatus(WalkRequestStatus status) {
        this.status = status;
    }
}
