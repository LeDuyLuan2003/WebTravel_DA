package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.*;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourScheduleRepository;
import com.SpringBootJdk22.SpringBootJdk22.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private TourService tourService;
    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService
    @Autowired
    private ContactService contactService;
    @Autowired
    private  ItemCategoryService itemCategoryService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private TourScheduleService tourScheduleService;

    @ModelAttribute
    public void addCategoriesToModel(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
    }
    @GetMapping("itemCategory/byCategory/{categoryId}")
    @ResponseBody
    public List<ItemCategory> getItemCategoriesByCategory(@PathVariable("categoryId") Long categoryId) {
        List<ItemCategory> itemCategories = itemCategoryService.findByCategoryId(categoryId);
        return itemCategories;
    }
    @GetMapping
    public String showTours(Model model) {
        List<Category> categories = categoryService.getAllCategories();

        for (Category category : categories) {
            // Lấy tất cả các tour trong danh mục
            List<Tour> allTours = new ArrayList<>();
            for (ItemCategory itemCategory : category.getItemCategories()) {
                List<Tour> tours = tourService.findByItemCategory(itemCategory);
                allTours.addAll(tours);
            }

            // Gán danh sách tour vào category (không chia nhóm)
            category.setAllTours(allTours);
        }

        model.addAttribute("categories", categories);
        return "users/home";
    }






    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        model.addAttribute("tour", tour);
        return "users/tour-detail";
    }
    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        return "users/about-us";
    }
    @GetMapping("/vehicle")
    public String showVehicle(Model model) {
        return "users/vehicle";
    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "users/contact";
    }

    @PostMapping("/contact")
    public String sendContact(@ModelAttribute Contact contact, Model model) {
        try {
            contactService.saveContact(contact);
            model.addAttribute("successMessage", "Your contact request has been sent successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was an error sending your contact request.");
        }
        return "redirect:/contact";
    }

    @GetMapping("/blog")
    public String showBlog(Model model) {
        return "/users/blog";
    }

    // Hiển thị form Booking
    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        // Lấy danh sách Tour và truyền vào model
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);

        // Tạo Booking object để bind dữ liệu từ form
        model.addAttribute("booking", new Booking());
        return "users/booking"; // Đường dẫn đến file Thymeleaf của form booking
    }

    // API: Lấy danh sách lịch trình (TourSchedule) theo Tour ID
    @GetMapping("/booking/schedules/{tourId}")
    @ResponseBody
    public List<TourSchedule> getSchedulesByTourId(@PathVariable Long tourId) {
        return tourScheduleService.findSchedulesByTourId(tourId);
    }

    // Lưu Booking
    @PostMapping("/booking")
    public String saveBooking(@ModelAttribute Booking booking, Model model) {
        try {
            bookingService.saveBooking(booking);
            model.addAttribute("successMessage", "Booking has been successfully submitted!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while processing your booking.");
        }
        return "redirect:/booking";
    }

    @GetMapping("/filter")
    public String filterTours(
                              @RequestParam(required = false) Long priceMin,
                              @RequestParam(required = false) Long priceMax,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String categoryName,
                              @RequestParam(required = false) String itemCategoryName,
                              Model model) {
        LocalDate parsedStartDate = null;
        if (startDate != null && !startDate.isEmpty()) {
            try {
                parsedStartDate = LocalDate.parse(startDate); // Chuyển từ String sang LocalDate
            } catch (DateTimeParseException e) {
                model.addAttribute("errorMessage", "Invalid start date format. Expected yyyy-MM-dd.");
                return "users/search"; // Trả về trang lỗi
            }
        }
        List<Tour> tours = tourService.filterTours(priceMin, priceMax, parsedStartDate, categoryName, itemCategoryName);
        model.addAttribute("tours", tours);
        return "users/search";
    }


    //exception
    @RequestMapping("/403")
    public String accessDenied() {
        return "403"; // Trả về tên của file HTML chứa nội dung lỗi 403
    }

}
