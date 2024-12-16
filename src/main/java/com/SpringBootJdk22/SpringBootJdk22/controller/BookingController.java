package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Booking;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourScheduleRepository;
import com.SpringBootJdk22.SpringBootJdk22.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TourScheduleRepository tourScheduleRepository;


}

