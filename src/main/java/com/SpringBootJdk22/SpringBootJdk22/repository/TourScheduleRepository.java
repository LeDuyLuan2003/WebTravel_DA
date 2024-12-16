package com.SpringBootJdk22.SpringBootJdk22.repository;

import com.SpringBootJdk22.SpringBootJdk22.model.TourSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourScheduleRepository extends JpaRepository<TourSchedule, Long> {
    List<TourSchedule> findByTourId(Long tourId);
}
