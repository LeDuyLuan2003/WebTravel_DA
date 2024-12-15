package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class TourSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour; // Liên kết với Tour
}
