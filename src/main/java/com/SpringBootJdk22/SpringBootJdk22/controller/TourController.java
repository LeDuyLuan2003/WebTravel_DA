package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Category;
import com.SpringBootJdk22.SpringBootJdk22.model.Image;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.ItemCategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("admin/tour")
public class TourController {
    @Autowired
    private TourService tourService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @GetMapping()
    public String showProductList(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/tours/tour-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/tours/add-tour";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Tour tour,
                             BindingResult result,
                             @RequestParam("avatarFile") MultipartFile avatarFile,
                             @RequestParam("imageFiles") MultipartFile[] imageFiles,
                             Model model) {
        if (result.hasErrors()) {
            System.out.println("price từ form khi add: " + tour.getPrice());
            System.out.println("finalprice từ form khi add: " + tour.getFinalPrice());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/tours/add-tour";
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
            Tour savedTour = tourService.addTour(tour);

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

        return "redirect:/admin/tour";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        // Lấy ItemCategory và Category của Tour này
        ItemCategory selectedItemCategory = tour.getItemCategory();
        Category selectedCategory = selectedItemCategory.getCategory();

        // Lấy danh sách ItemCategory của Category đã chọn
        List<ItemCategory> itemCategories = itemCategoryService.findByCategoryId(selectedCategory.getId());

        // Thêm dữ liệu vào model để hiển thị trên giao diện
        model.addAttribute("tour", tour);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", selectedCategory); // Category hiện tại
        model.addAttribute("itemCategories", itemCategories);     // Danh sách ItemCategory thuộc Category

        return "admin/tours/update-tour";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute Tour tour,
                                BindingResult result,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                @RequestParam("imageFiles") MultipartFile[] imageFiles,
                                Model model) {
        if (result.hasErrors()) {
            tour.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/tours/update-tour";
        }

        try {
            Tour existingTour = tourService.getTourById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));

            // Update avatar if provided
            if (!avatarFile.isEmpty()) {
                String avatarFileName = saveFile(avatarFile);
                existingTour.setAvatar(avatarFileName);
            }

            // Update fields and recalculate finalPrice
            existingTour.setName(tour.getName());
            existingTour.setPrice(tour.getPrice());
            existingTour.setPerson(tour.getPerson());
            existingTour.setDiscountPercentage(tour.getDiscountPercentage());
            existingTour.setDescription(tour.getDescription());
            existingTour.setItemCategory(tour.getItemCategory());
            existingTour.setFinalPrice(tour.getDiscountPercentage() > 0
                    ? tour.getPrice() - (tour.getPrice() * tour.getDiscountPercentage() / 100)
                    : tour.getPrice());

            // Save updated tour
            Tour updatedTour = tourService.updateTour(existingTour);

            // Save new images
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String imageFileName = saveFile(imageFile);
                    Image image = new Image();
                    image.setUrl(imageFileName);
                    image.setTour(updatedTour);
                    tourService.addImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("price từ form khi update: " + tour.getPrice());
        System.out.println("finalprice từ form khi update: " + tour.getFinalPrice());
        return "redirect:/admin/tour";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        tourService.deleteTourById(id);
        return "redirect:/admin/tour";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        model.addAttribute("tour", tour);
        return "/admin/tours/tour-detail";
    }

    @GetMapping("/search")
    public String searchProductsByName(@RequestParam("name") String name, Model model) {
        List<Tour> searchResults = tourService.findToursByName(name);
        model.addAttribute("products", searchResults);
        return "admin/tours/tour-list";
    }

    // Save file to "static/uploads"
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String uploadDir = "/home/binhan/cicd-deploy/uploads/";
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }

}
