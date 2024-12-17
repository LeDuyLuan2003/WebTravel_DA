package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.Booking;
import com.SpringBootJdk22.SpringBootJdk22.model.TourSchedule;
import com.SpringBootJdk22.SpringBootJdk22.repository.BookingRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;

    // Lưu booking
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    // Lấy tất cả các booking (nếu cần danh sách cho admin quản lý)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Lấy booking theo ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Xóa booking
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}

