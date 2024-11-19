package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Contact;
import com.SpringBootJdk22.SpringBootJdk22.model.Image;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.ContactService;
import com.SpringBootJdk22.SpringBootJdk22.service.ItemCategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private TourService tourService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ContactService contactService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @GetMapping
    public String employee() {
        return "/layout-employee";
    }

    @GetMapping("/tour")
    public String showProductList(Model model) {
        model.addAttribute("tours", tourService.getAllProducts());
        return "/employee/tours/tour-list";
    }


    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Tour tour = tourService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        model.addAttribute("tour", tour);
        return "/users/tour-detail";
    }

    @GetMapping("/tour/add")
    public String showAddForm(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/employee/tours/add-tour";
    }

    @PostMapping("/tour/add")
    public String addProduct(@Valid @ModelAttribute Tour tour,
                             BindingResult result,
                             @RequestParam("avatarFile") MultipartFile avatarFile,
                             @RequestParam("imageFiles") MultipartFile[] imageFiles,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "/employee/tours/add-tour";
        }

        // Calculate finalPrice based on discountPercentage and price
        if (tour.getDiscountPercentage() > 0) {
            tour.setFinalPrice(tour.getPrice() - (tour.getPrice() * tour.getDiscountPercentage() / 100));
        } else {
            tour.setFinalPrice(tour.getPrice());
        }

        try {
            // Save avatar image
            if (!avatarFile.isEmpty()) {
                String avatarFileName = saveFile(avatarFile);
                tour.setAvatar(avatarFileName);
            }

            // Save tour information in database
            Tour savedTour = tourService.addProduct(tour);

            // Save additional images related to the tour
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String imageFileName = saveFile(imageFile);
                    Image image = new Image();
                    image.setUrl(imageFileName);
                    image.setTour(savedTour);
                    tourService.addImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/employee/tour";
    }

    @GetMapping("/contact")
    public String showContactList(Model model) {
        List<Contact> contacts = contactService.getAllContacts();
        model.addAttribute("contacts", contacts);
        return "employee/contacts/list-contact"; // Tên file HTML để hiển thị danh sách
    }

    @PostMapping("/contact/updateStatus")
    public String updateContactStatus(@RequestParam("contactId") Long contactId,
                                      @RequestParam("responded") boolean responded) {
        Contact contact = contactService.getContactById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact ID: " + contactId));
        contact.setResponded(responded);
        contactService.saveContact(contact); // Lưu trạng thái mới vào DB
        return "redirect:/employee/contact"; // Quay lại trang danh sách
    }


    // Save file to "static/uploads"
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads", fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return fileName;
    }





}
