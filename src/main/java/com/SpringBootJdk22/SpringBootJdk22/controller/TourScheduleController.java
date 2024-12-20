package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.TourSchedule;
import com.SpringBootJdk22.SpringBootJdk22.service.TourScheduleService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tourSchedules")
public class TourScheduleController {

    @Autowired
    private TourScheduleService tourScheduleService;

    @Autowired
    private TourService tourService;

    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("schedules", tourScheduleService.getAllSchedules());
        return "admin/TourSchedule/schedule-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("schedule", new TourSchedule());
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/TourSchedule/schedule-form";
    }

    @PostMapping("/new")
    public String addSchedule(@ModelAttribute TourSchedule schedule) {
        tourScheduleService.addSchedule(schedule);
        return "redirect:/admin/tourSchedules";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        TourSchedule schedule = tourScheduleService.getAllSchedules()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Schedule Not Found"));

        model.addAttribute("schedule", schedule);
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/TourSchedule/schedule-form";
    }

    @PostMapping("/update/{id}")
    public String updateSchedule(@PathVariable Long id, @ModelAttribute TourSchedule updatedSchedule) {
        tourScheduleService.updateSchedule(id, updatedSchedule);
        return "redirect:/admin/tourSchedules";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        tourScheduleService.deleteSchedule(id);
        return "redirect:/admin/tourSchedules";
    }
}

