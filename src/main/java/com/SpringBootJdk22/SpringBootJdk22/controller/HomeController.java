package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private TourService tourService;
    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService

    @ModelAttribute
    public void addCategoriesToModel(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
    }

    // Display a list of all products
    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("tours", tourService.getAllProducts());
        return "/users/home";
    }
    @GetMapping("/search")
    public String searchProductsByName(@RequestParam("name") String name, Model model) {
        List<Tour> searchResults = tourService.findProductsByName(name);
        model.addAttribute("tours", searchResults);
        return "/users/home"; // Template dùng cho người dùng
    }


    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Tour tour = tourService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        model.addAttribute("tour", tour);
        return "/users/tour-detail";
    }
    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        return "/users/about-us";
    }
    @GetMapping("/vehicle")
    public String showVehicle(Model model) {
        return "/users/vehicle";
    }
    @GetMapping("/contact")
    public String showContact(Model model) {
        return "/users/contact";
    }

    //exception
    @RequestMapping("/403")
    public String accessDenied() {
        return "/403"; // Trả về tên của file HTML chứa nội dung lỗi 403
    }

}