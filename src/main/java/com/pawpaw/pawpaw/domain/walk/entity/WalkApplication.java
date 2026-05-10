package com.pawpaw.pawpaw.domain.walk.entity;

import com.pawpaw.pawpaw.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "walk_applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"walk_request_id", "user_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WalkApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_request_id", nullable = false)
    private WalkRequest walkRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WalkApplicationStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime appliedAt;

    public void updateStatus(WalkApplicationStatus status) {
        this.status = status;
    }
}
