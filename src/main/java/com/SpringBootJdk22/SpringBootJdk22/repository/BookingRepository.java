package com.SpringBootJdk22.SpringBootJdk22.repository;

import com.SpringBootJdk22.SpringBootJdk22.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
