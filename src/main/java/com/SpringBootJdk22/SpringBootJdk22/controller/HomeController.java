package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Category;
import com.SpringBootJdk22.SpringBootJdk22.model.Contact;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.ContactService;
import com.SpringBootJdk22.SpringBootJdk22.service.ItemCategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    // Display a list of all products
    @GetMapping
    public String showTours(Model model) {
        List<Category> categories = categoryService.getAllCategories();

        for (Category category : categories) {
            List<Tour> allTours = new ArrayList<>();
            for (ItemCategory itemCategory : category.getItemCategories()) {
                List<Tour> tours = tourService.findByItemCategory(itemCategory);
                allTours.addAll(tours);
            }
            category.setAllTours(allTours); // Tạo getter/setter mới cho `allTours` trong `Category`.
        }

        model.addAttribute("categories", categories);
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
        model.addAttribute("contact", new Contact());
        return "/users/contact";
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




    //exception
    @RequestMapping("/403")
    public String accessDenied() {
        return "/403"; // Trả về tên của file HTML chứa nội dung lỗi 403
    }

}