package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID của booking

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour; // Tour được đăng ký

    @ManyToOne
    @JoinColumn(name = "tour_schedule_id", nullable = false)
    private TourSchedule schedule; // Lịch trình được chọn cho tour

    @Column(nullable = false)
    private String fullName; // Họ và tên người dùng

    @Column(nullable = false)
    private String email; // Email người dùng

    @Column(nullable = false)
    private String phoneNumber; // Số điện thoại người dùng

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate; // Ngày đăng ký (tự động)

    @Column(name = "number_of_people", nullable = false)
    private int numberOfPeople; // Số lượng người tham gia

    @Column(name = "status", nullable = false)
    private String status = "Pending"; // Trạng thái của booking mặc định là "Pending"

    @PrePersist
    public void prePersist() {
        this.bookingDate = LocalDate.now(); // Gán ngày đăng ký hiện tại khi lưu vào DB
    }
}
