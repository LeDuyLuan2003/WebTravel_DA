package com.SpringBootJdk22.SpringBootJdk22.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class TourSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID của lịch trình

    @Column(nullable = false)
    private LocalDate startDate; // Ngày bắt đầu

    @Column(nullable = false)
    private LocalDate endDate; // Ngày kết thúc

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    @JsonBackReference
    private Tour tour; // Liên kết với tour
}
