package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.TourSchedule;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourScheduleService {

    @Autowired
    private TourScheduleRepository tourScheduleRepository;

    // Lấy tất cả TourSchedule
    public List<TourSchedule> getAllSchedules() {
        return tourScheduleRepository.findAll();
    }

    // Thêm mới TourSchedule
    public TourSchedule addSchedule(TourSchedule schedule) {
        return tourScheduleRepository.save(schedule);
    }

    // Cập nhật TourSchedule
    public TourSchedule updateSchedule(Long id, TourSchedule updatedSchedule) {
        return tourScheduleRepository.findById(id).map(schedule -> {
            schedule.setStartDate(updatedSchedule.getStartDate());
            schedule.setEndDate(updatedSchedule.getEndDate());
            schedule.setTour(updatedSchedule.getTour());
            return tourScheduleRepository.save(schedule);
        }).orElseThrow(() -> new RuntimeException("Schedule not found with id " + id));
    }

    // Xóa TourSchedule
    public void deleteSchedule(Long id) {
        tourScheduleRepository.deleteById(id);
    }
}
