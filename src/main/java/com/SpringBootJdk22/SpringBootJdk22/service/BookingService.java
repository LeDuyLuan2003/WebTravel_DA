package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.Booking;
import com.SpringBootJdk22.SpringBootJdk22.model.TourSchedule;
import com.SpringBootJdk22.SpringBootJdk22.repository.BookingRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private TourScheduleRepository tourScheduleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Long tourId, Long scheduleId, Booking booking) {
        // Lấy thông tin lịch trình từ TourSchedule
        TourSchedule schedule = tourScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("TourSchedule not found"));

        // Sao chép thông tin lịch trình sang Booking
        booking.setStartDate(schedule.getStartDate());
        booking.setEndDate(schedule.getEndDate());

        // Lưu Booking
        return bookingRepository.save(booking);
    }
}
